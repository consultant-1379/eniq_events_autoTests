#!/bin/bash
# ----------------------------------------------------------------------
# Ericsson Network IQ Directory Structure CHECK for CPG Streaming
#
# Usage:
#
#       ./checkEniqCPGdir.sh
#
#
# ----------------------------------------------------------------------
# Copyright (c) 1999 - 2007 AB Ericsson Oy  All rights reserved.
# ---------------------------------------------------------------------

# 0 = PASS >0 is fail
TEST_STATUS=0;
PASS=0
FAIL=1


function myExit
{
  # exsure TEST_STATUS has been set in main bash 
  if [ $TEST_STATUS -eq $PASS ] ; then
    echo
    echo "DIRECTORY STRUCTURE: TEST PASSED"
    exit 0
  else
    echo
    echo "DIRECTORY STRUCTURE: TEST FAILED"
    exit 1
  fi
 
}


# *****************************************************************************************************
#  CPG stream files


myParentDir="/eniq/data/pmdata/eventdata/"
for g in "00" "01" "02" "03" "04" "05" "06" "07" "08" "09" "10" "11" "12" "13" "14" "15"
do
     myDir1=$myParentDir$g"/cpg_event_cpgs_file/"
     echo
     echo CHECKING $myDir1
     
     dir1minOK=$FAIL
     dir15minOK=$FAIL
     dir5minOK=$FAIL
     
     nohead1minOK=$FAIL
     nohead15minOK=$FAIL
     nohead5minOK=$FAIL
     
     for gg in $( ls $myDir1 );
     do
         myDir2=$myDir1$gg
         myDir2noHeader=$myDir1$gg"/noheader"
         
         if [ $gg = "1min" ] ; then
             dir1minOK=$PASS
             echo $myDir2 "..........exists: PASS"
             if [ -d $myDir2noHeader ] ; then
                echo $myDir2noHeader ".exists: PASS" 
                nohead1minOK=$PASS
             fi
         else   
             if [ $gg = "15min" ] ; then
                 dir15minOK=$PASS
                 echo $myDir2 ".........exists: PASS"
                 if [ -d $myDir2noHeader ] ; then
                    echo $myDir2noHeader "exists: PASS"
                    nohead15minOK=$PASS
                 fi
             else   
                 if [ $gg = "5min" ] ; then
                     dir5minOK=$PASS
                     echo $myDir2 "..........exists: PASS"
                     if [ -d $myDir2noHeader ] ; then
                        echo $myDir2noHeader ".exists: PASS"
                         nohead5minOK=$PASS
                     fi
                 fi
             fi
         fi   
     done
     if [ $dir1minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"1min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi

     if [ $dir15minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"15min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
 
     if [ $dir5minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"5min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
     
     if [ $nohead1minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"1min/noheader does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
     
     if [ $nohead15minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"15min/noheader does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
     
     if [ $nohead5minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"5min/noheader does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
     
     if [ $TEST_STATUS -eq $FAIL ] ; then
        echo "."
        echo  "Unknown Fail Reason: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi   
done


# *****************************************************************************************************
#  CPG DB loader files success

myParentDir="/eniq/data/pmdata/eventdata/"
for g in "00" "01" "02" "03" "04" "05" "06" "07" "08" "09" "10" "11" "12" "13" "14" "15"
do
     myDir1=$myParentDir$g"/cpg_event_db_loader/success/"
     echo
     echo CHECKING $myDir1
     
     db1minOK=$FAIL
     db15minOK=$FAIL
     db5minOK=$FAIL

     
     for gg in $( ls $myDir1 );
     do
         myDir2=$myDir1$gg
         
         if [ $gg = "1min" ] ; then
             db1minOK=$PASS
             echo $myDir2 "...exists: PASS"
         else   
             if [ $gg = "15min" ] ; then
                 db15minOK=$PASS
                 echo $myDir2 "..exists: PASS"
             else   
                 if [ $gg = "5min" ] ; then
                     db5minOK=$PASS
                     echo $myDir2 "...exists: PASS"
                 fi
             fi
         fi   
     done
     if [ $db1minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"1min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi

     if [ $db15minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"15min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
 
     if [ $db5minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"5min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
          
     if [ $TEST_STATUS -eq $FAIL ] ; then
        echo "."
        echo  "Unknown Fail Reason: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi   
done
# *****************************************************************************************************
#  CPG DB loader files failure

myParentDir="/eniq/data/pmdata/eventdata/"
for g in "00" "01" "02" "03" "04" "05" "06" "07" "08" "09" "10" "11" "12" "13" "14" "15"
do
     myDir1=$myParentDir$g"/cpg_event_db_loader/failure/"
     echo
     echo CHECKING $myDir1
     
     db1minOK=$FAIL
     db15minOK=$FAIL
     db5minOK=$FAIL

     
     for gg in $( ls $myDir1 );
     do
         myDir2=$myDir1$gg
         
         if [ $gg = "1min" ] ; then
             db1minOK=$PASS
             echo $myDir2 "...exists: PASS"
         else   
             if [ $gg = "15min" ] ; then
                 db15minOK=$PASS
                 echo $myDir2 "..exists: PASS"
             else   
                 if [ $gg = "5min" ] ; then
                     db5minOK=$PASS
                     echo $myDir2 "...exists: PASS"
                 fi
             fi
         fi   
     done
     if [ $db1minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"1min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi

     if [ $db15minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"15min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
 
     if [ $db5minOK -eq $FAIL ] ; then
        echo "."
        echo  $myDir1"5min ..does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
          
     if [ $TEST_STATUS -eq $FAIL ] ; then
        echo "."
        echo  "Unknown Fail Reason: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi   
done

# *****************************************************************************************************
#  Inter-workflow directory (CPG)


myParentDir="/eniq/data/pmdata/eventdata/"
echo
echo
echo
echo CHECKING $myParentDir"/xx/CPG_M/cpg_iwf_dir/cpg"
for g in "00" "01" "02" "03" "04" "05" "06" "07" "08" "09" "10" "11" "12" "13" "14" "15"
do
     myDir1=$myParentDir$g"/CPG_M/cpg_iwf_dir/"
     myDir2=$myDir1$"cpg"
         
     if [ -d $myDir2 ] ; then
         echo $myDir2 ".............exists: PASS" 
     else 
        echo "."
        echo  $myDir2 ".....does not exist: FAIL"
        TEST_STATUS=$FAIL;
        myExit
     fi
done

# *****************************************************************************************************

# CPG stream - symbolic links
myDir1=$myParentDir"01/CPG_M/cpg/" 
echo
echo
echo
echo CHECKING $myDir1
 
dir1minOK=$FAIL
dir15minOK=$FAIL
dir5minOK=$FAIL
 
nohead1minOK=$FAIL
nohead15minOK=$FAIL
nohead5minOK=$FAIL
 
 
for gg in $( ls $myDir1 );
do
     myDir2=$myDir1$gg
     myDir2noHeader=$myDir1$gg"/noheader"
        
     
     if [ $gg = "1min" ] ; then
         dir1minOK=$PASS
         echo $myDir2 "...................exists: PASS"
         if [ -d $myDir2noHeader ] ; then
            echo $myDir2noHeader "..........exists: PASS" 
            nohead1minOK=$PASS
         fi
     else   
         if [ $gg = "15min" ] ; then
             dir15minOK=$PASS
             echo $myDir2 "..................exists: PASS"
             if [ -d $myDir2noHeader ] ; then
                echo $myDir2noHeader ".........exists: PASS" 
                nohead15minOK=$PASS
             fi
         else   
             if [ $gg = "5min" ] ; then
                 dir5minOK=$PASS
                 echo $myDir2 "...................exists: PASS"
                 if [ -d $myDir2noHeader ] ; then
                    echo $myDir2noHeader "..........exists: PASS" 
                    nohead5minOK=$PASS
                 fi
             fi
         fi
     fi   
done
if [ $dir1minOK -eq $FAIL ] ; then
    echo "."
    echo  $myDir1"1min does not exist: FAIL"
    TEST_STATUS=$FAIL;
    myExit
fi

if [ $dir15minOK -eq $FAIL ] ; then
    echo "."
    echo  $myDir1"15min does not exist: FAIL"
    TEST_STATUS=$FAIL;
    myExit
fi

if [ $dir5minOK -eq $FAIL ] ; then
    echo "."
    echo  $myDir1"5min does not exist: FAIL"
    TEST_STATUS=$FAIL;
    myExit
fi

if [ $nohead1minOK -eq $FAIL ] ; then
   echo "."
   echo  $myDir1"1min/noheader does not exist: FAIL"
   TEST_STATUS=$FAIL;
   myExit
 fi
     
if [ $nohead15minOK -eq $FAIL ] ; then
    echo "."
    echo  $myDir1"15min/noheader does not exist: FAIL"
    TEST_STATUS=$FAIL;
    myExit
 fi
 
 if [ $nohead5minOK -eq $FAIL ] ; then
    echo "."
    echo  $myDir1"5min/noheader does not exist: FAIL"
    TEST_STATUS=$FAIL;
    myExit
 fi
     

if [ $TEST_STATUS -eq $FAIL ] ; then
    echo "."
    echo  "Unknown Fail Reason: FAIL"
    TEST_STATUS=$FAIL;
    myExit
fi   

# *****************************************************************************************************

# Aggregation session cache folder (CPG)

myDir2=$myParentDir"01/CPG_M/cpg_aggregation/cpg/" 
echo
echo
echo
echo CHECKING $myDir2
 
if [ -d $myDir2 ] ; then
   echo $myDir2 ".............exists: PASS" 
else 
   echo "."
   echo  $myDir2 ".....does not exist: FAIL"
   TEST_STATUS=$FAIL;
   myExit
fi 
 
# *****************************************************************************************************


myExit


