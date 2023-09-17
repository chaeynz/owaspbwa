<?php

require_once("genclude.php");
require_once("offscanpages/statistics/utilities.php");

// a little bit of a check!!!
$existingFiles = listScanFiles();
if(count($existingFiles) > 3000){
  echo "<b>ERROR: Too many statistics collected on the system. Please contact the author: urgunb at hotmail dot com</b>";
  return;
}

// store this url as visited if it's not already visited
//$tokens = split("\?",basename($_SERVER['REQUEST_URI']));
//$tokens[0] = trim($tokens[0]);
//echo $_SERVER['REQUEST_URI']. "<br/>";
//echo basename($_SERVER['REQUEST_URI']). "<br/>";
//echo "$tokens[0]<br/>";
//$justphpname = substr($tokens[0], 0, strlen($tokens[0]) - 4);

$phpbasename = basename($_SERVER['SCRIPT_NAME']);
$justphpname = substr($phpbasename, 0, strlen($phpbasename) - 4);

if(!isset($_SESSION['currenturlsvisited'][$justphpname])){
  // initialize
  $_SESSION['currenturlsvisited'][$justphpname] = 
    array("noofaccess" => 1, 
          "timefirstaccess" => time(),
          "timelastaccess" => time(),
          "useragent" => $_SERVER['HTTP_USER_AGENT'],
          "ipaddress" => $_SERVER['REMOTE_ADDR']
          );

  scanToFile($_SESSION['time'], $_SESSION['currenturlsvisited']);  
}
else{
  /*
   currenturlsvisited's structure
    every element's key is REQUEST_URI and value is array X
    there are one or elements in value array X;
      
      key: noofaccess
      value: how many times this link is fetched in this session
      
      key: timefirstaccess
      value: the time the link is fetched

      key: timelastaccess      
      value: the time the link is fetched
      array_shift(explode('?', basename($_SERVER['REQUEST_URI'])))
      (split("\?",basename($_SERVER['REQUEST_URI'])))[0]
  */
  $visitedurl = $_SESSION['currenturlsvisited'][$justphpname];
  $visitedurl["noofaccess"] = $visitedurl["noofaccess"] + 1;
  $visitedurl["timelastaccess"] = time();   
  $_SESSION['currenturlsvisited'][$justphpname] = $visitedurl;

  scanToFile($_SESSION['time'], $_SESSION['currenturlsvisited']);
}

?>


