#!/bin/bash

##############################################################################
# training.sh
#
# Usage:
#   training.sh [-s source] [-t target] [-f first] [-l last]
#   where
#     -s source: Source language. Default: en.
#     -t target: Target language. Default: es.
#     -f first: First step in training process. Default: 1.
#     -l last: Last step in training process. Default: 9.
##############################################################################


# Constants
BASE_DIR=/home/cristinae/
BASE_DATA=/media/cristinae/DATA1/pln/
DEF_SRC=en
DEF_TRG=fr
DEF_TYPE=titles
DEF_PREFIX=pubPsych.$DEF_TYPE.train
DEF_FIRST=1
DEF_LAST=6
LANGS=(x en es de fr x)

# BINARIES
MOSESDECODER=$BASE_DIR/soft/mosesdecoder
NGRAM_COUNT=$BASE_DIR/soft/srilm/bin/i686-m64/ngram-count
MOSES_SCRIPTS=$MOSESDECODER/scripts
CLEAN_CORPUS=$MOSES_SCRIPTS/training/clean-corpus-n.perl
TRAIN_MODEL=$MOSES_SCRIPTS/training/train-model.perl
TOKENIZER=$MOSES_SCRIPTS/tokenizer/tokenizerNO2html.perl
MOSES_BIN=$MOSESDECODER/bin/moses
MERT_DIR=$MOSESDECODER/bin/
MOSES_BIN_TOOLS=$MOSESDECODER/tools
MERT_MOSES=$MOSES_SCRIPTS/training/mert-moses.pl

# Functions
print_usage()
{
  echo "Usage:"
  echo "  training.sh [-s source] [-t target]"
  echo "  where"
  echo "    -s source: Source language. Default: en."
  echo "    -t target: Target language. Default: es."
  echo "    -p prefix: Corpus prefix. Default: pubPsych.abstracts.train"
  echo "    -f first: First step in training process. Default: 1."
  echo "    -l last: Last step in training process. Default: 9."
}

# MAIN START
src=$DEF_SRC
trg=$DEF_TRG
if [ $src = "en" ]; then
    lang=$trg
else
    lang=$src
fi

prefix=${DEF_PREFIX}.en-$lang.lem
first=$DEF_FIRST
last=$DEF_LAST
dir=$BASE_DATA/experiments/clubs/queries/smt/
dirCorpus=$BASE_DATA/corpora/pubPsych/$DEF_TYPE/
translator=pubpsych${DEF_TYPE}Lem

while getopts ":s:t:p:f:l:" opt
do
  case "${opt}" in
     s)
       src=$OPTARG
       echo "-s was triggered!"
       ;;
     t)
       trg=$OPTARG
       echo "-t was triggered!"
       ;;
     f)
       first=$OPTARG
       echo "-f was triggered!"
       ;;
     l)
       last=$OPTARG
       echo "-l was triggered!"
       ;;
     p)
       prefix=$OPTARG
       echo "-p was triggered!"
       ;;
     \?)
       echo "Invalid option: -$OPTARG"
       print_usage;
       exit 1;
       ;;
     :)
       echo "Option -$OPTARG requires an argument"
       print_usage;
       exit 1;
       ;;   
  esac
done


# Check languages
if [[ "${LANGS[*]}" != *" $src "* ]];
then
  echo "Unknown language: $lang"
  print_usage;
  exit 1;
fi

# Check training steps
# first > 0 & last <= 9 & first <= last
if [[ $first -le 0 ]];
then
  echo 'The first training step must be greater than 0'
  print_usage;
  exit 1;
fi

if [[ $last -gt 9 ]];
then
  echo 'The last training step must be less than or equal to 9'
  print_usage;
  exit 1;
fi

if [[ $first -gt $last ]];
then
  echo 'The first step must less than or equal to the last step.'
  print_usage;
  exit 1;
fi

# Initialize variables
dirTrad="$dir/$translator/$src-$trg"
corpus_name="$dirCorpus/$prefix"
corpus_clean="$corpus_name.clean"
lm_dir="$dir/lm"
lm_trg="$lm_dir/$prefix.$trg.5.lm"
#dev_name="$dir/tests/newstest2012.tc"

# Create target language model
if [[ -f $lm_trg ]];
then
  echo "Language model $lm_trg already exists."
else
  echo "Creating model language $lm_trg"
  mkdir -p $lm_dir
  $NGRAM_COUNT -order 5 -interpolate -kndiscount -text $corpus_name.$trg -lm $lm_trg
fi

# Clean corpora
clean_corpus=1
if [[ -f "$corpus_clean.$src" ]];
then
  if [[ -f "$corpus_clean.$trg" ]];
  then
    echo "The files with the cleaned corpus already exist."
    clean_corpus=0
  fi
fi

if [[ $clean_corpus -eq 1 ]];
then
  echo "Cleaning corpora"
  rm -f $corpus_clean*
  $CLEAN_CORPUS $corpus_name $src $trg $corpus_clean 1 100
fi

# Train translator
out="$dirTrad"
mkdir -p $out

echo "Training translator. Output: $out"
 $TRAIN_MODEL --parallel -scripts-root-dir $MOSES_SCRIPTS -root-dir $out -corpus $corpus_clean -f $src -e $trg -alignment grow-diag-final-and -reordering msd-bidirectional-fe -lm 0:5:$lm_trg:0 -external-bin-dir $MOSES_BIN_TOOLS --first-step=$first --last-step=$last  --score-options '--GoodTuring'

# MERT (Tuning translator)
# tuning="$out/tuning100"
# model="$out/model"
# mkdir -p $tuning

# echo "Tuning the translator (MERT). Output: $tuning"
# $MERT_MOSES $dev_name.$src $dev_name.$trg --working-dir=$tuning  --nbest=100  $MOSES_BIN $model/moses.ini --mertdir $MERT_DIR
# $MERT_MOSES $dev_name.$src $dev_name.$trg --working-dir=$tuning --continue  --nbest=100  $MOSES_BIN $model/moses.ini --mertdir $MERT_DIR

