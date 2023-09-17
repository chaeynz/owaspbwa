<?php
  require_once('../genclude.php');
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="content-type" content="text/html; charset=windows-1250">
  <link type="text/css" href="../style.css" rel="stylesheet"/>
  <script src="../js/yahoo/yahoo-min.js"></script>
  <script src="../js/yahoo/event-min.js"></script>
	<script src="../js/yahoo/connection-min.js"></script>
	<script>

            var busy = 0;

            // string prototyping :)
            String.prototype.trim = function() {
                   return this.replace(/^\s+|\s+$/g,"");
            }

            var AjaxObject = {

              handleSuccess:function(o){
                  busy = 0;
                  this.processResult(o);
              },

              handleFailure:function(o){
                  busy = 0;
                  // Failure handler
              },

              processResult:function(o){
                  var container = document.getElementById("container");
                  container.innerHTML = o.responseText.trim();
              },

              startRequest:function(page) {
                 YAHOO.util.Connect.asyncRequest('GET', page, callback, null);
              }

            };

            var callback =
            {
                   success: AjaxObject.handleSuccess,
                   failure: AjaxObject.handleFailure,
                   scope: AjaxObject
            }

            function doxhr(phpname){              
              if(!busy){
                AjaxObject.startRequest(formURL(phpname));
                busy = 1;
              }
              else
                  alert('not so fast, one request at a time old sport');
            }
      
            function formURL(urlendingpart){
                return "../innerpages/"  + urlendingpart + ".php";
            }
	</script>
  </head>
  <body  class="body">    
    <center>
      <div id="container"></div>
      <br/>
      <br/>
      <input type="button" value="click me 1" onclick="doxhr('17_143ef')"/>
      <input type="button" value="click me 2" onclick="doxhr('17_2da76')"/>
    </center>
  </body>
</html>
