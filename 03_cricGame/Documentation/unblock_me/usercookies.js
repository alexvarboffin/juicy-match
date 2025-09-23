function UserCookies(a) {
    var b = window.localStorage;
    this.set = function (e) {
        var f = 3600 * 24 * 90;
        if (b) {
            localStorage.setItem(a, "" + e);
            return
        }
        var c = "";
        if (typeof (f) != "undefined") {
            var d = new Date();
            d.setTime(d.getTime() + (f * 4000));
            var c = "; expires=" + d.toGMTString()
        }
        document.cookie = a + "=" + e + c + "; path=/"
    };
    this.get = function (f) {
        if (b) {
            var g = localStorage.getItem(a);
            return (g && g != "") ? g : f
        }
        var j = a + "=";
        var d = document.cookie.split(";");
        for (var e = 0; e < d.length; e++) {
            var h = d[e];
            while (h.charAt(0) == " ") {
                h = h.substring(1, h.length)
            }
            if (h.indexOf(j) == 0) {
                return h.substring(j.length, h.length)
            }
        }
        return f
    };
    this.remove = function () {
        this.setCookie(a, "", -1)
    };
    return this
}

function getPlatForm() {
    var platform = "Desktop";

    if (navigator.userAgent.match(/Android/i)) {
        return "Android";
    }

    if (navigator.userAgent.match(/BlackBerry/i)) {
        return "BlackBerry";
    }

    if (navigator.userAgent.match(/iPhone/i)) {
        return "iPhone";
    }

    if (navigator.userAgent.match(/iPad|iPod/i)) {
        return "iPad";
    }

    if (navigator.userAgent.match(/Opera Mini/i)) {
        return "Opera";
    }

    if (navigator.userAgent.match(/IEMobile/i)) {
        return "Windows";
    }

    return platform;
}

var usercookies = new UserCookies("usercookies");
var userName = usercookies.get("");
//var comServiceUrl = "http://localhost:50514/api/";
var comServiceUrl = "https://duanzi.azurewebsites.net/api/";
function syncGameScore(gameName, score, saveLocalScore) {
    //debugger;
    if (userName == "")
        return;

    var platForm = getPlatForm();

    var serviceUrl = comServiceUrl + "Score";

    $.ajax({
        type: "post",
        url: serviceUrl,
        data: { "GameName": gameName, "UserName": userName, "GameScore": score, "PlatForm": platForm },
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        var dbScore = data.GameScore;

        // Json - 2 Pics 1 Word
        if (data.Game.ScoreType == "4")
        {
            if (dbScore.length > score.length) {
                saveLocalScore(dbScore);
            }
            if (dbScore.length == score.length &&
                dbScore > score)
            {
                saveLocalScore(dbScore);
            }
        }
        // Json - Jagsaw Puzzle
        else if (data.Game.ScoreType=="3") {
            if (dbScore.split("true").length > score.split("true").length) {
                saveLocalScore(dbScore);
            }
        }
        // Json - Word Search
        else if (data.Game.ScoreType == "2") {
            if (dbScore.length > score.length) {
                saveLocalScore(dbScore);
            }
        }
        else {
            if (parseInt(dbScore) > parseInt(score)) {
                saveLocalScore(dbScore);
            }
        }
    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        //alert(jqXHR.responseText || textStatus);
    });
}

function getGameScore(gameName, score, saveLocalScore) {
    //debugger;
    if (userName == "")
        return;

    var platForm = getPlatForm();

    var serviceUrl = comServiceUrl + "Score/GetScore";

    $.ajax({
        type: "post",
        url: serviceUrl,
        data: { "GameName": gameName, "UserName": userName, "GameScore": score, "PlatForm": platForm },
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        var dbScore = data.GameScore;

        // Json - 2 Pics 1 Word
        if (data.Game.ScoreType == "4") {
            if (dbScore.length > score.length) {
                saveLocalScore(dbScore);
            }
            if (dbScore.length == score.length &&
                dbScore > score) {
                saveLocalScore(dbScore);
            }
        }
        // Json - Jagsaw Puzzle
        else if (data.Game.ScoreType == "3") {
            if (dbScore.split("true").length > score.split("true").length) {
                saveLocalScore(dbScore);
            }
        }
        // Json - Word Search
        else if (data.Game.ScoreType == "2") {
            if (dbScore.length > score.length) {
                saveLocalScore(dbScore);
            }
        }
        else {
            if (parseInt(dbScore) > parseInt(score)) {
                saveLocalScore(dbScore);
            }
        }
    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        //alert(jqXHR.responseText || textStatus);
    });
}

function getLeaderBoard(bind) {

    var serviceUrl = comServiceUrl + "Score";

    $.ajax({
        type: "get",
        url: serviceUrl,
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        bind(data);
    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        //alert(jqXHR.responseText || textStatus);
    });
}

// Google Analytics
//(function (i, s, o, g, r, a, m) {
//    i['GoogleAnalyticsObject'] = r; i[r] = i[r] || function () {
//        (i[r].q = i[r].q || []).push(arguments)
//    }, i[r].l = 1 * new Date(); a = s.createElement(o),
//    m = s.getElementsByTagName(o)[0]; a.async = 1; a.src = g; m.parentNode.insertBefore(a, m)
//})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

//ga('create', 'UA-61615726-1', 'auto');
//ga('send', 'pageview');

function enableGoogleAnalytics() {
    var html5_script = document.createElement('script');

    html5_script.onload = function () {
        window.dataLayer = window.dataLayer || [];
        function gtag() { dataLayer.push(arguments); }
        gtag('js', new Date());

        gtag('config', 'G-LPVH5H7R6S');
    };

    html5_script.setAttribute('src', 'https://www.googletagmanager.com/gtag/js?id=G-LPVH5H7R6S');
    html5_script.setAttribute('async', 'true');

    document.head.appendChild(html5_script);

}
enableGoogleAnalytics();

function enableAdBlocking() {
    var html5_script = document.createElement('script');

    html5_script.onload = function () {
        function signalGooglefcPresent() {
            if (!window.frames['googlefcPresent']) {
                if (document.body) {
                    const iframe = document.createElement('iframe');
                    iframe.style = 'width: 0; height: 0; border: none; z-index: -1000; left: -1000px; top: -1000px;';
                    iframe.style.display = 'none';
                    iframe.name = 'googlefcPresent';
                    document.body.appendChild(iframe);
                } else {
                    setTimeout(signalGooglefcPresent, 0);
                }
            }
        }
        signalGooglefcPresent();
    };

    html5_script.setAttribute('src', 'https://fundingchoicesmessages.google.com/i/pub-6289839502769272?ers=1');
    html5_script.setAttribute('async', 'true');
    //html5_script.setAttribute('nonce', '5wnF_SS65xAVyh_KnOLJXg');

    document.head.appendChild(html5_script);
}
enableAdBlocking();

function getAllGames(bind) {

    var serviceUrl = comServiceUrl + "Game";

    $.ajax({
        type: "get",
        url: serviceUrl,
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        bind(data);
    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        //alert(jqXHR.responseText || textStatus);
    });
}

// Start - Function for Forum

function getForumMain(bind) {
    $.ajax({
        type: "get",
        url: comServiceUrl + "Threads",
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        bind(data);
    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        //alert(jqXHR.responseText || textStatus);
    });
}

function getForumDetail(bind, threadId, title) {
    $.ajax({
        type: "get",
        url: comServiceUrl + "Threads/" + threadId,
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        bind(data, title, comServiceUrl + "Posts/");
    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        //alert(jqXHR.responseText || textStatus);
    });
}

function submitPost(dataForm, file) {
    if (userName == "")
        return;

    $.ajax({
        type: "post",
        url: comServiceUrl + "Posts",
        data: { "ThreadId": dataForm.ThreadId, "UserName": userName, "CategoryId": dataForm.CategoryId, "Title": dataForm.Title, "Body": dataForm.Body },
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //alert("done");
        var ajaxRequest = $.ajax({
            type: "POST",
            url: comServiceUrl + "Threads/uploadfile/" + data.PostId,
            contentType: false,
            processData: false,
            data: file
        }).done(function (data) {

            if (dataForm.ThreadId == -1) {
                window.location.href = 'forum.html';
            }
            else {
                window.location.href = 'forum.html?threadId=' + threadId;
            }

        }).error(function (jqXHR, textStatus, errorThrown) {
            //alert("error");
        });

    }).error(function (jqXHR, textStatus, errorThrown) {
        //alert("error");
        //alert(jqXHR.responseText || textStatus);
    });
}

// End - Funciont for Forum

function showHideHomeIcon()
{
    var $homeIcon;

    if (!document.getElementById('homeIcon')) {
        var $homeIcon = $("<img id='homeIcon' src='/img/home.png' style='display:inline-block;width:50px;height:50px;position:fixed; cursor:pointer; bottom:0px; right:0px; z-index:999990;'/>");
        $homeIcon.click(function () {
            //var confirmed = confirm("Do you want to navigate to home page?");
            var confirmed = true;
            if (confirmed) {

                if (self == top)
                    //window.location.href = "/index.html";
                    window.location.href = "../../";
                else
                    //window.parent.location.href = "/index.html";
                    window.parent.location.href = "../../";
            }
        });

        $("body").append($homeIcon);
    }
    else {
        $homeIcon = $("#homeIcon");
    }

    var width = $(window).width();
    var height = $(window).height();

    if (width >= 1024 && width > height) {
    //if (width >= 1024 && height > 720 && width > height) {
        $homeIcon.show();
    }
    else {
        $homeIcon.hide();
    }
}

$(window).on("resize", function () {

    showHideHomeIcon();

});

$(document).on("ready", function () {

    redirectCloudFront();

    showHideHomeIcon();
    $('head').append('<link rel="stylesheet" href="/css/share.css" type="text/css" />');

    try { getXpPlayedGame(); } catch (err) { };

    setTimeout(function () {
        try { $('#at4-scc').click(); } catch (err) { };
    }, 5000);
});

function redirectCloudFront() {

    checkInIframe();

    if (window.location.hostname == 'localhost')
        return;

    var url = window.location.href;

    if (window.location.protocol == 'http:')
        url = url.replace('http:', 'https:');

    if (window.location.hostname.indexOf('www') == -1)
        url = url.replace(window.location.hostname, 'www.' + window.location.hostname);

    if (url != window.location.href) {
        //console.log(url);
        window.location.href = url;
    }
}

function checkInIframe() {
    if (self !== top) {
        alert("Illegal Use!");
        window.location.href = "https://www.google.com";
    }
}

function getXpPlayedGame() {
    var url = window.location.href;
    var urlArray = url.split('/games/');

    if (urlArray.length == 2) {
        var currentGame = urlArray[1];
        var playedGameList = JSON.parse(localStorage.getItem("xpPlayedGame"));

        if (playedGameList == null) {
            playedGameList = [];
        }
        else {
            for (var i = 0; i < playedGameList.length; i++) {
                if (playedGameList[i] == currentGame) {
                    playedGameList.splice(i, 1);
                    break;
                }
            }
        }

        playedGameList.unshift(currentGame);

        localStorage.setItem("xpPlayedGame", JSON.stringify(playedGameList));

        //console.log(JSON.stringify(playedGameList));
    }
}

function addFaceBookUser(name, email, showRegisterP) {

    if (email == null)
        return;

    var success = false;

    var password = "???";//set on server side
    //debugger;
    //var serviceUrl = 'https://commonapi.azurewebsites.net/api/User';
    var serviceUrl = comServiceUrl + 'User/FaceBookUser';
    $.ajax({
        type: "post",
        url: serviceUrl,
        data: { "Name": email, "Email": email, "Password": password, "ProjectId": '4' },// save email as name to avoid duplication
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        success = true;

        //var usercookies = new UserCookies("usercookies");
        usercookies.set(data.Name);

        userName = usercookies.get("");

        showRegisterP();
        //boolServerBack = true;
        //console.log("success!!!!!!");
        //var alevel = g$.store.getInt("alevel", 0);
        //getGameScore("FreeCell Solitaire", alevel, saveLocalScore);

    }).error(function (jqXHR, textStatus, errorThrown) {
        if (jqXHR.status == 400) {

            if (jqXHR.responseText.indexOf("Please choose") > -1) {
                console.log(jqXHR.responseJSON.Message);
            }
            else {
                console.log(jqXHR.responseText || textStatus);
            }
        }
        else {
            console.log(jqXHR.responseText || textStatus);
        }
    });

    return success;
}

function writeGameLog(plateform, account, gameName, otherInfo) {

    var success = false;

    //debugger;

    var serviceUrl = comServiceUrl + 'GameLog';

    $.ajax({
        type: "post",
        url: serviceUrl,
        data: { "Plateform": plateform, "Account": account, "GameName": gameName, "OtherInfo": otherInfo, "Who": userName },
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {

        success = true;

        console.log("success!!!");

    }).error(function (jqXHR, textStatus, errorThrown) {
        //console.log(jqXHR.responseText || textStatus);
        console.log("error!!!");
    });

    return success;
}

function forceResetScore(gameName, score) {
    //debugger;
    if (userName == "")
        return;

    var platForm = getPlatForm();

    var serviceUrl = comServiceUrl + "Score/ForceResetScore";

    $.ajax({
        type: "post",
        url: serviceUrl,
        data: { "GameName": gameName, "UserName": userName, "GameScore": score, "PlatForm": platForm },
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        //console.log(data.GameScore);
 
    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        //alert(jqXHR.responseText || textStatus);
    });
}

function forceResetScore2(gameName, score, callBack) {
    //debugger;
    if (userName == "")
    {
        callBack();
        return;
    }

    var platForm = getPlatForm();

    var serviceUrl = comServiceUrl + "Score/ForceResetScore";

    $.ajax({
        type: "post",
        url: serviceUrl,
        data: { "GameName": gameName, "UserName": userName, "GameScore": score, "PlatForm": platForm },
        dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        //console.log(data.GameScore);
        callBack();

    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        //alert(jqXHR.responseText || textStatus);
    });
}

function prepareTopScores() {

    var serviceUrl = comServiceUrl + "Score/PrepareScore";

    $.ajax({
        type: "post",
        url: serviceUrl,
        //data: { "GameName": gameName, "UserName": userName, "GameScore": score, "PlatForm": platForm },
        //dataType: "json",
        accept: 'application/json'
    }).done(function (data) {
        //debugger;
        console.log(data);

    }).error(function (jqXHR, textStatus, errorThrown) {
        //debugger;
        console.log(jqXHR.responseText || textStatus);
    });
}