$(function() {
    Social.setUp();
    Tracker.setUp();
    Streaming.setUp();
});

var Streaming = {
    setUp: function() {
        if (pusherID !== '') {
            $('.clicka,.auth').hide();
            $('.steps li:eq(0)').removeClass('active');
            $('.steps li:eq(1)').addClass('active');
            
            $('.thanks').show();
            $('.lepel').show().css({opacity: 1});
            setTimeout(function() {
                $('.thanks').hide();
                $('.lepel').css({opacity: 0});
                $('.streama,.activity').fadeIn();
                $('.email').html('for ' + ownerEmail).fadeIn();
                setTimeout(function(){ $('.lepel').hide() }, 300);
            }, 1000);
            
            var pusher = new Pusher(pusherKey);
            var channel = pusher.subscribe(pusherChannel);
            channel.bind(pusherID, function(entries) {
                for (var i in entries) {
                    var e = entries[i];
                    var img, name;
                    if (e.deleted) {
                        img = 'trash.png';
                        name = e.fileName;
                    } else if (e.directory) {
                        img = 'folder.png';
                        name = e.fileName;
                    } else {
                        img = 'document.png';
                        name = '<a href="' + e.downloadUrl + '">' + e.fileName + '</a>';
                    }
                    var html = '<p style="opacity: 0"><img src="img/'+img+'" style="width:20px; opacity: 0.7; margin-right: 20px; position: relative; top: 3px;" />'+name+'</p>';
                    $('.activity').prepend(html);
                    setTimeout(function(){ $('.activity p').css('opacity', 1); }, 100);
                }
            });
        }
    }
}

// the mandatory tracker
var Tracker = {
    setUpGoogleAnalytics : function() {
        window._gaq = window._gaq || [];
        window._gaq.push(['_setAccount', 'UA-31383816-1']);
        window._gaq.push(['_trackPageview']);
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    },
    setUp : function() {
        this.setUpGoogleAnalytics();
    }
};

// the mandatory social buttons
var Social = {
    setUpPlusButton : function() {
        var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
        po.src = 'https://apis.google.com/js/plusone.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
    },
    setUpLikeButton : function() {
        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) return;
            js = d.createElement(s); js.id = id;
            js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=302001246494798";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));        
    },
    setUpTweetButton : function() {
        (function(d,s,id){
            var js,fjs=d.getElementsByTagName(s)[0];
            if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";
            fjs.parentNode.insertBefore(js,fjs);}
        })(document,"script","twitter-wjs");
    },
    setUp : function() {
        this.setUpPlusButton();
        this.setUpTweetButton();
        this.setUpLikeButton();
    }   
};
