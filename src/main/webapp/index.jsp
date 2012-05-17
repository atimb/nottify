<%@ page import="com.nottify.notify.NotificationSender" %>
<!doctype html>
<html lang="en" itemscope itemtype="http://schema.org/Horseshit">
    <head>
    	<meta charset="utf-8">
    	<title>Nottify - Track your Google Drive activity</title>
    	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    	<meta name="apple-mobile-web-app-capable" content="yes">
    	<meta name="viewport" content="width=960" />
        <meta itemprop="name" content="Nottify">
        <meta itemprop="description" content="Track your Google Drive activity">
        <meta itemprop="image" content="http://nottify.herokuapp.com/img/logo.png">
        <meta property="fb:admins" content="100000335626436" />
        <meta property="og:url" content="http://nottify.herokuapp.com/">
        <meta property="og:type" content="website">
        <meta property="og:title" content="Nottify" />
        <meta property="og:description" content="Track your Google Drive activity" />
        <meta property="og:image" content="http://nottify.herokuapp.com/img/logo_big.png" />
    	<link rel="shortcut icon" href="favicon.ico" />
        <link href='http://fonts.googleapis.com/css?family=Londrina+Sketch|Unkempt|Nova+Flat' rel='stylesheet' type='text/css'>
        <link rel="stylesheet/less" type="text/css" href="css/style.less">
        <script type="text/javascript">
            var pusherID = '<%=(request.getSession().getAttribute("credential") != null)?request.getSession().getId():"" %>';
            var pusherKey = '<%=NotificationSender.PUSHER_KEY %>';
            var pusherChannel = '<%=NotificationSender.PUSHER_CHANNEL %>';
            var ownerEmail = '<%=request.getSession().getAttribute("email") %>';
        </script>
        <script type="text/javascript" src="js/less-1.2.1.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js"></script>
        <script src="http://js.pusher.com/1.11/pusher.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/base.js"></script>
        <!--[if lt IE 9]><script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
    </head>
    <body>
        <header>
            <div class="container">
                <img src="img/thunder.png" class="logo" />
                <h1>Nottify - Track your Google Drive activity</h1>
                <div class="social">
                    <g:plusone size="medium"></g:plusone>
                    <a href="https://twitter.com/share" class="twitter-share-button" data-url="http://nottify.herokuapp.com" data-text="Track your Google Drive activity"></a>
                    <div class="fb">
                        <div class="fb-like" data-href="http://nottify.herokuapp.com" data-send="false" data-layout="button_count" data-show-faces="false"></div>
                    </div>
                </div>
            </div>
        </header>
        <div class="main">
            <ul class="steps">
                <li data-step="1" class="active">OAuthorize with Google</li>
                <li data-step="2">See your Google Drive changes</li>
            </ul>
            <div class="drop-area">
                <div class="drop-inner">
                    <p class="clicka">Please click the button below</p>
                    <a class="auth" href="/authorize"><input type="button" value="Authorize"></a>
                    <p class="streama" style="display: none">Live stream for Google Drive activity</p>
                    <p class="email" style="display: none"></p>
                    <p class="thanks" style="display: none">Thanks!</p>
                    <div class="activity" style="display: none"></div>
                    <div class="demo">
                        <img src="img/tutorial.png" />
                    </div>
                </div>
            </div>
        </div>
        <footer>
            This is a heroku | pusher | gdrive experiment for <a href="http://www.cloudspokes.com">CloupSpokes</a>
        </footer>
        <div class="lepel" style="display: none"></div>
        <div id="fb-root"></div>
    </body>
</html>
