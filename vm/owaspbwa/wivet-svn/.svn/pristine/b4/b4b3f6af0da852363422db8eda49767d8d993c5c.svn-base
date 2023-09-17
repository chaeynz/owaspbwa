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
    
            // string prototyping :)
            String.prototype.trim = function() {
                   return this.replace(/^\s+|\s+$/g,"");
            }

            var AjaxObject = {

              handleSuccess:function(o){
                  this.processResult(o);
              },

              handleFailure:function(o){
                  // Failure handler
              },

              processResult:function(o){
                  var container = document.getElementById("container");
                  container.innerHTML = o.responseText.trim();
              },

              startRequest:function() {
                 YAHOO.util.Connect.asyncRequest('GET', '../innerpages/13_10ad3.php', callback, null);
              }

            };

            var callback =
            {
                   success: AjaxObject.handleSuccess,
                   failure: AjaxObject.handleFailure,
                   scope: AjaxObject
            }

            function doxhr(){
             AjaxObject.startRequest();
            }
      
	</script>
  </head>
  <body  class="body">    
    <center>
      <div id="container"></div>
      <br/>
      <br/>
      <input type="button" value="click me" onclick="doxhr()"/>
    </center>
  </body>
</html>
