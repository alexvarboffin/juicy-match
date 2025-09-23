if (!window.g$) {
    window.g$ = {}
}
g$.isAndroid = false;//(/android/gi).test(navigator.appVersion) && window.Score;
g$.iOS = false;//(/iphone|ipad|ipod/gi).test(navigator.appVersion) && !(/Safari/gi).test(navigator.appVersion);
g$.Metro = (/MSAppHost/gi).test(navigator.appVersion);
g$.isMobile = g$.isAndroid || g$.iOS;
g$.isDesktop = !g$.isMobile;
g$.Lanscape = g$.Lanscape || false;
adsbygoogle = null;
(function () {
    if (!g$.iOS) {
        return
    }
    document.documentElement.style.webkitTouchCallout = "none";
    var b = $(document.createElement("iframe")).hide(),
        e = $(window);
    document.documentElement.appendChild(b[0]);

    function h(l, j) {
        try {
            b.attr("src", "app://" + l + "?" + j.join("&"))
        } catch (k) {}
    }
    var c = {},
        i = window.location.search;
    if (i.length > 0) {
        i = i.substring(1)
    }
    $.each(i.split("&"), function (k, l) {
        var j = l.split("=");
        c[j[0]] = j[1]
    });
    var d = parseInt(c.ht) || $(window).height(),
        a = parseInt(c.wd) || $(window).width(),
        f = ((a > d) ? (a / 480) : (a / 320)) || 1;
    g$.WinHeight = d / f;
    g$.WinWidth = a / f;
    g$.adHeight = parseInt(c.ad || 0) / f;
    g$.ClientHeight = g$.WinHeight - g$.adHeight;
    window.Score = {
        iOSparam: "",
        isBackProc: function () {
            return false
        },
        Trace: function (j) {
            h("log", [j])
        },
        MoreGames: function () {
            h("appstore", [""])
        },
        Quit: function () {},
        sndClear: function () {
            h("sndClear", [])
        },
        sndStop: function (j) {
            h("sndStop", [j])
        },
        sndPlay: function (j, k) {
            h("sndPlay", [j, k || 1])
        }
    };
    var g = $(document.createElement("meta")).attr({
        name: "viewport",
        content: "width=" + g$.WinHeight + ", initial-scale=" + f + ", minimum-scale=" + f + ", maximum-scale=" + f + ", users-scalable=no"
    });
    document.getElementsByTagName("head")[0].appendChild(g[0]);
    document.ontouchmove = function (j) {
        j.preventDefault()
    };
    //window.setInterval(function () {
    //    document.body.scrollTop = document.body.scrollLeft = 0
    //}, 1000)
})();
(function () {
    if (!g$.isAndroid) {
        return
    }
    g$.WinWidth = parseInt(window.Score.WinWidth()) || 320;
    g$.WinHeight = parseInt(window.Score.WinHeight()) || 483;
    g$.ClientHeight = parseInt(window.Score.ClientHeight()) || 433;
    $(document).ready(function () {
        $(document.body).bind(g$.touchDown, function () {})
    })
})();
(function () {
    g$.bodyResize = true;
    var e = null,
        d = 0,
        c = null;
    g$.body = function () {
        if (!g$.isDesktop || !document.body) {
            return document.body
        }
        if (!e) {
            var f = $("body > *").remove();
            e = $("<div style='position:absolute'></div>").appendTo(document.body).append(f)
        }
        c = (g$.Landscape) ? {
            x: 900,
            y: 520
        } : {
            x: 450,
            y: 700
        };
        g$.WinWidth = c.x;
        g$.ClientHeight = (g$.WinHeight = c.y) - (g$.adHeight = 0);
        return e[0]
    };
    if (!g$.isDesktop) {
        return
    }
    g$.ClientHeight = g$.WinHeight - (g$.adHeight = 0);

    function b() {
        try {
            g$.extCss(g$.body().style, {
                TransformOrigin: "0px 0px"
            });
            var j = $(window),
                f = j.width(),
                i = j.height();
            g$.bodyScale = 1;
            g$.bodyOffset = 0;
            if (1 || g$.bodyResize) {
                g$.bodyScale = Math.min(f / g$.WinWidth, Math.max(430, i) / g$.WinHeight);
                
                g$.bodyOffset = ~~((f - g$.WinWidth * g$.bodyScale) / 2) - 1;
            }
            $(g$.body()).css({
                scale: g$.bodyScale,
                margin: "0px " + g$.bodyOffset + "px",
                width: g$.WinWidth,
                height: g$.WinHeight
            });
            if (g$.popup.bkg) {
                g$.popup.bkg.css({
                    width: ~~(f / g$.bodyScale),
                    height: ~~(i / g$.bodyScale),
                    margin: "0px " + ~~(-g$.bodyOffset / g$.bodyScale) + "px"
                })
            }
            if (g$.TitleBar) {
                g$.TitleBar.container.css({
                    width: ~~(f / g$.bodyScale),
                    margin: "0px " + ~~(-g$.bodyOffset / g$.bodyScale) + "px"
                })
            }
        } catch (g) {}
    }

    function a() {
        d = d || window.setTimeout(function () {
            d = 0;
            b()
        }, 1)
    }
    $(window).resize(a).ready(function () {
        g$.body();

        //var adsLeft = 0;

        //if ($(window).width() >= 728) {
        //    adsLeft = ($(window).width() - 728) / 2;

        //    (adsbygoogle = window.adsbygoogle || []).push({});
        //    //alert($(window).width());
        //    $("body").append('<ins class="adsbygoogle" style="display:inline-block;width:728px;height:90px;position:fixed; bottom:0px; left:' + adsLeft + 'px; z-index:999992" data-ad-client="ca-pub-6289839502769272" data-ad-slot="7973335316"></ins>');
        //    addDocNode("script", {
        //        type: "text/javascript",
        //        src: "https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"
        //    });
        //}

        if ($(window).width() >= 1024) {
            var adsLeft = 0;
            adsLeft = ($(window).width() - 728) / 2;

            (adsbygoogle = window.adsbygoogle || []).push({});
            $("body").append('<ins class="adsbygoogle" style="display:inline-block;width:728px;height:90px;position:fixed; bottom:0px; left:' + adsLeft + 'px; z-index:999992" data-ad-client="ca-pub-6289839502769272" data-ad-slot="7973335316"></ins>');
            addDocNode("script", {
                type: "text/javascript",
                src: "https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"
            });
        } else {
            (adsbygoogle = window.adsbygoogle || []).push({});
            $("body").append('<div style="bottom:0px; width:100%;position:fixed;text-align: center;z-index:999992;height:50px;"><ins class="adsbygoogle" style="display:inline-block;vertical-align:top;width:320px;height:50px;" data-ad-client="ca-pub-6289839502769272" data-ad-slot="7973335316"></ins></div>');
            addDocNode("script", {
                type: "text/javascript",
                src: "https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"
            });
        }

        b();
    })
})();
(function (b) {
    var a = document.createElement("div");
    a.setAttribute("ontouchend", "return;");
    g$.touchDown = typeof (a.ontouchend) == "function" ? "touchstart" : "mousedown";
    g$.touchMove = typeof (a.ontouchend) == "function" ? "touchmove" : "mousemove";
    g$.touchUp = typeof (a.ontouchend) == "function" ? "touchend" : "mouseup";
    g$.touchOut = typeof (a.ontouchend) == "function" ? "touchcancel" : "mouseout";
    //todo
    //if (b.browser.mozilla) {
    //    window.g$.stylePrefix = "Moz"
    //} else {
    //    if (b.browser.webkit) {
    //        window.g$.stylePrefix = "webkit"
    //    } else {
    //        if (b.browser.msie) {
    //            window.g$.stylePrefix = "Ms"
    //        }
    //    }
    //}
    g$.cssTransitions = a.style[g$.stylePrefix + "Transition"] !== undefined;
    if (typeof (console) == "undefined") {
        console = {}
    }
    if (window.Score && window.Score.Trace) {
        console.log = function (c) {
            window.Score.Trace("" + c)
        }
    }
    if (!console.log) {
        console.log = function (c) {}
    }
    g$.WinWidth = g$.WinWidth || 320;
    g$.WinHeight = g$.WinHeight || 483;
    g$.ClientHeight = g$.ClientHeight || 433;
    g$.adHeight = g$.WinHeight - g$.ClientHeight;
    g$.bodyScale = 1;
    g$.bodyOffset = 0
})(jQuery);
(function () {
    var a = jQuery.fn.offset;
    jQuery.fn.offset = function (b) {
        var c = a.apply(this, arguments);
        if (g$.bodyOffset && this && this[0] != g$.body()) {
            c.top /= g$.bodyScale;
            c.left = (c.left - g$.bodyOffset) / g$.bodyScale
        }
        return c
    }
})();

function addDocNode(c, b, f) {
    var e = document.createElement(c);
    for (var d in b) {
        e[d] = b[d]
    }
    f = (f || document.getElementsByTagName("head")[0]);
    f.insertBefore(e, f.childNodes[0])
}

g$.extCss = function (a, c) {
    for (var b in c) {
        a[b.replace(/^[A-Z]/, function (d) {
            return d.toLowerCase()
        })] = a[g$.stylePrefix + b] = c[b]
    }
    return a
};
String.prototype.format = function () {
    var a = arguments;
    return this.replace(/{(\d+)}/g, function (b, c) {
        return typeof a[c] != "undefined" ? a[c] : "{" + c + "}"
    })
};
String.prototype.endsWith = function (a) {
    return this.length >= a.length && this.indexOf(a, this.length - a.length) != -1
};
String.prototype.startsWith = function (a) {
    return this.indexOf(a) == 0
};
String.prototype.endsWith = function (a) {
    return this.indexOf(a, this.length - a.length) !== -1
};
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (b, d) {
        var a = this.length;
        for (var c = d || 0; c < a; c++) {
            if (this[c] == b) {
                return c
            }
        }
        return -1
    }
}
if (!Array.prototype.filter) {
    Array.prototype.filter = function (b, f) {
        var e = this,
            a = e.length,
            d = [];
        for (var c = 0; c < a; c++) {
            if (b.call(f, e[c], c, e)) {
                d.push(e[c])
            }
        }
        return d
    }
}
if (!Array.prototype.reduce) {
    Array.prototype.reduce = function reduce(b) {
        var c = 0,
            a = this.length >> 0,
            d;
        if (arguments.length < 2) {
            if (a === 0) {
                throw new TypeError("Array length is 0 and no second argument")
            }
            d = this[0];
            c = 1
        } else {
            d = arguments[1]
        }
        for (; c < a; c++) {
            d = b(d, this[c], c, this)
        }
        return d
    }
}
if (!Array.prototype.map) {
    Array.prototype.map = function (f, c) {
        var e, b = [],
            a = this.length;
        if (c) {
            e = c
        }
        for (var d = 0; d < a; d++) {
            b[d] = f.call(e, this[d], d, this)
        }
        return b
    }
}
if (!Array.prototype.toArray) {
    Array.prototype.toArray = function () {
        return this
    }
}
Array.prototype.remove = function (b) {
    var a = this.indexOf(b);
    if (a >= 0) {
        this.splice(a, 1)
    }
    return b
};
Array.prototype.last = function () {
    return this.length ? this[this.length - 1] : null
};
Array.prototype.unique = function () {
    var b = [];
    for (var a = 0; a < this.length; a++) {
        if (b.indexOf(this[a]) < 0) {
            b.push(this[a])
        }
    }
    return b
};
Array.prototype.rand = function () {
    return Math.floor(Math.random() * this.length)
};
Array.prototype.randItem = function () {
    return this[Math.floor(Math.random() * this.length)]
};
Array.prototype.randomize = function (a) {
    for (var c = (a || this.length) - 1; c >= 0; c--) {
        var b = this[c],
            d = Math.floor(Math.random() * this.length);
        this[c] = this[d];
        this[d] = b
    }
    return this
};
Array.prototype.init = function (a, c) {
    this.length = a;
    for (var b = 0; b < a; b++) {
        this[b] = typeof (c) == "function" ? c(b) : ($.isArray(c) ? [] : c)
    }
    return this
};
Array.prototype.initNum = function (d, a) {
    var c = [];
    for (var b = d; b <= a; b++) {
        c.push(b)
    }
    return c
};
Array.prototype.bSearch = function (c, b) {
    var e = 0,
        d = this.length - 1,
        a;
    while (e <= d) {
        a = (e + d) >> 1;
        if (this[a] < c) {
            e = a + 1
        } else {
            if (this[a] > c) {
                d = a - 1
            } else {
                return a
            }
        }
    }
    return b ? e : -1
};
Math.rand = function (a, b) {
    return a + Math.floor(Math.random() * (b - a + 1))
};
Math.cntBits = function (a) {
    a = a - ((a >> 1) & 1431655765);
    a = (a & 858993459) + ((a >> 2) & 858993459);
    return (((a + (a >> 4)) & 252645135) * 16843009) >> 24
};
g$._b2dLookup = [1, 2, 29, 3, 30, 15, 25, 4, 31, 23, 21, 16, 26, 18, 5, 9, 32, 28, 14, 24, 22, 20, 17, 8, 27, 13, 19, 7, 12, 6, 11, 10];
Math.bitNum = function (a) {
    if (!a || a != (a & -a)) {
        return 0
    }
    return g$._b2dLookup[(a * 125613361) >>> 27]
};
Math.sign = function (a) {
    return a > 0 ? 1 : ((a < 0) ? -1 : 0)
};
Math.range = function (c, b, a) {
    return Math.max(c, Math.min(b, a))
};
var JSON = JSON || {};
(function () {
    function a(b) {
        return "'" + b.replace(/'/g, "\\'") + "'"
    }
    JSON.parse = JSON.parse || function (b) {
        return jQuery.parseJSON(b)
    };
    JSON.stringify = JSON.stringify || function (f) {
        var e = typeof (f);
        if (e != "object" || f === null) {
            if (e == "string") {
                f = a(f)
            }
            return String(f)
        }
        var g, c, d = [],
            b = (f && f.constructor == Array);
        for (g in f) {
            c = f[g];
            e = typeof (c);
            if (e == "function") {
                continue
            } else {
                if (e == "string") {
                    c = a(c)
                } else {
                    if (e == "object" && c !== null) {
                        c = JSON.stringify(c)
                    }
                }
            }
            d.push((b ? "" : '"' + g + '":') + String(c))
        }
        return (b ? "[" : "{") + String(d) + (b ? "]" : "}")
    }
}());

function Cookies(a) {
    var b = !g$.iOS && window.localStorage;
    this.set = function (e, f) {
        if (b) {
            localStorage.setItem(a, "" + e);
            return
        }
        var c = "";
        if (typeof (f) != "undefined") {
            var d = new Date();
            d.setTime(d.getTime() + (f * 1000));
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

function ShowLives(e, d) {
    var b = "",
        a = parseInt(d);
    while (a--) {
        b += "&hearts;"
    }
    document.getElementById(e).innerHTML = b
}

function num2str(b) {
    b = b.toString();
    for (var a = /(\d+)(\d{3})/; a.test(b);) {
        b = b.replace(a, "$1,$2")
    }
    return b
}(function (a) {
    g$.store = {};
    g$.store.getString = function (e, b) {
        b = b || "";
        var d = document.title || "";
        if (!window.Score || !window.Score.getProp) {
            return new Cookies(d + e).get(b)
        }
        var c = window.Score.getProp(e);
        return (c && c != "") ? c : b
    };
    g$.store.getInt = function (c, b) {
        return 0 + parseInt(g$.store.getString(c, "" + b))
    };
    g$.store.getObj = function (f, e) {
        var c = g$.store.getString(f, "");
        if (!c) {
            return e
        }
        try {
            var b = JSON.parse(c);
            return (b || b === 0 || b === "") ? b : e
        } catch (d) {
            return e
        }
    };
    g$.store.set = function (d, b) {
        var c = document.title || "";
        if (window.Score && window.Score.setProp) {
            window.Score.setProp(d, "" + b)
        } else {
            new Cookies(c + d).set(b, 3600 * 24 * 90)
        }
        return b
    };
    a.store = g$.store
})(jQuery);
(function (c) {
    g$.popup = {
        dialog: null,
        bkg: null,
        uid: 0,
        msg: null,
        orgPar: null,
        scroller: null,
        onClose: []
    };
    var b = null;

    function a() {
        if (b) {
            b = window.clearTimeout(b)
        }
        b = setTimeout(function () {
            b = null;
            if (g$.popup.dialog) {
                return
            }
            for (var d = g$.popup.onClose.length - 1; d >= 0; d--) {
                g$.popup.onClose[d]()
            }
        }, 300)
    }
    g$.popup.close = function () {
        if (g$.popup.dialog) {
            if (g$.popup.msg && g$.popup.orgPar && g$.popup.orgPar.length) {
                g$.popup.msg.appendTo(g$.popup.orgPar).hide()
            }
            if (g$.popup.scroller) {
                g$.popup.scroller.destroy()
            }
            g$.popup.dialog.remove();
            g$.popup.msg = g$.popup.orgPar = g$.popup.dialog = null;
            if (g$.TitleBar) {
                g$.TitleBar.focus()
            }
            a()
        }
        g$.popup.bkg.hide()
    };
    g$.popup.show = function (j) {
        if (g$.popup.dialog) {
            g$.popup.close()
        }
        g$.popup.uid++;
        var g = g$.WinWidth,
            e = null;
        j = c.extend({
            title: "",
            msg: "",
            tmo: 0,
            b1Text: "",
            b1Click: g$.popup.close,
            b2Text: "",
            b2Click: g$.popup.close
        }, j);
        if (!g$.popup.bkg) {
            g$.popup.bkg = c("<div style='position:absolute; background-color:black; '></div>").css({
                left: 0,
                top: 0,
                zIndex: 999990,
                margin: 0
            }).appendTo(g$.body())
        }
        g$.popup.bkg.css({
            opacity: 0.7,
            width: g,
            height: g$.WinHeight
        });
        g$.popup.dialog = c("<div style='position:absolute; display:inline-block; z-index:999991;'></div>").css({
            backgroundColor: "#e0e0e0",
            border: "outset 3px"
        }).appendTo(g$.body());
        if (j.title) {
            c("<div class='button' style='display:block;text-align:left;'></div>").html(j.title).appendTo(g$.popup.dialog)
        }
        if (j.msg) {
            e = c("<div style='margin:3px; overflow:hidden'></div>");
            if (typeof (j.msg) == "object" || (typeof (j.msg) == "string" && j.msg.substring(0, 1) == "#")) {
                g$.popup.orgPar = (g$.popup.msg = c(j.msg)).parent();
                e.append(g$.popup.msg.show()).appendTo(g$.popup.dialog)
            } else {
                e.html("<div>" + j.msg + "</div>").appendTo(g$.popup.dialog)
            }
            e.css("width", Math.min(e.width(), g * 0.95))
        }
        if (j.b1Text) {
            var i = c("<div style='float:right; margin:3px;'></div>").appendTo(g$.popup.dialog);
            c("<div class='round button shadow' style='width:90px' />").html(j.b1Text).bind(g$.touchDown, j.b1Click).appendTo(i);
            if (j.b2Text) {
                c("<div class='round button shadow' style='width:90px; margin:0 0 0 10px;' />").html(j.b2Text).bind(g$.touchDown, j.b2Click).appendTo(i)
            }
        } else {
            j.tmo = j.tmo || 1000
        }
        var k = g$.popup.dialog.height(),
            f = e.height() + 6;
        if (e) {
            var d = k - g$.ClientHeight + 6;
            e.css("height", (c("[maxheight]", e).length || d > 0) ? f - d : f);
            if (d > 0) {
                e.css({
                    overflowY: "scroll"
                })
            }
            if (!c.browser.msie && typeof (iScroll) != "undefined") {
                try {
                    k = g$.popup.dialog.height();
                    g$.popup.scroller = new iScroll(e[0], {
                        hScroll: false,
                        onBeforeScrollStart: function (l) {
                            this.refresh();
                            l.preventDefault()
                        }
                    });
                    window.setTimeout(function () {
                        g$.popup.scroller.scrollTo(0.1, 0.1, 1)
                    }, 50)
                } catch (h) {}
            }
        }
        g$.popup.dialog.css({
            top: Math.max(0, (g$.ClientHeight - k) / 2),
            left: (g - g$.popup.dialog.outerWidth()) / 2
        });
        if (j.tmo) {
            window.setTimeout(function () {
                if (g$.popup.dialog) {
                    g$.popup.dialog.animate({
                        opacity: 0
                    }, 300);
                    g$.popup.bkg.animate({
                        opacity: 0
                    }, 300, j.b1Click)
                }
            }, j.tmo)
        }
        g$.popup.dialog.add(g$.popup.bkg).show();
        c(window).trigger("resize");
        if (g$.TitleBar) {
            g$.TitleBar.focus()
        }
        g$.sound.bgCheck(500)
    };
    window.alert = window.alert || function (d) {
        g$.popup.show({
            msg: d,
            b1Text: "OK"
        })
    };
    c.popup = g$.popup
})(jQuery);
(function (c) {
    var b = g$.store.getInt("mute", 1);
    var d = {},
        e = null,
        a = null;
    g$.sound = {
        bgSound: [],
        clear: function () {
            if (window.Score && window.Score.sndClear) {
                window.Score.sndClear()
            } else {
                for (var h in d) {
                    for (var f = d[h], g = f.length - 1; g >= 0; g--) {
                        f[g].pause();
                        delete f[g]
                    }
                }
            }
            d = {};
            e = null;
            return g$.sound
        },
        play: function (h, k) {
            if (b || !h) {
                return g$.sound
            }
            try {
                k = k || 1;
                if (window.Score && window.Score.sndPlay) {
                    return window.Score.sndPlay(h, "" + k)
                }
                if (!d[h]) {
                    d[h] = []
                }
                if (d[h].length < k || (k < 1 && !d[h].length)) {
                    var f = new Audio(h);
                    d[h].push(f);
                    f.addEventListener("ended", function () {
                        g$.sound.onDone(h)
                    }, false)
                }
                for (var g = d[h].length - 1; g >= 0; g--) {
                    var f = d[h][g];
                    if (f.ended || !f.currentTime || f.paused) {
                        if (k < 1) {
                            f.loop = true
                        }
                        f.play()
                    }
                }
            } catch (j) {
                console.log(j)
            }
            return g$.sound
        },
        menu: function () {
            if (!window.Score || !window.Score.sndPlay) {
                return null
            }
            return {
                type: "check",
                text: "Sound",
                val: b,
                list: [0, 1],
                click: function (f) {
                    g$.sound.clear();
                    g$.store.set("mute", b = f);
                    g$.sound.bgCheck()
                }
            }
        },
        stop: function (f) {
            if (!f) {
                return
            }
            if (window.Score && window.Score.sndStop) {
                window.Score.sndStop(f)
            } else {
                if (d[f]) {
                    jQuery.each(d[f],
                        function (h, g) {
                            if (!g.ended && g.currentTime && !g.paused) {
                                g.pause()
                            }
                        })
                }
            }
            return g$.sound
        },
        onDone: function (f) {
            if (f != e) {
                return
            }
            if (g$.popup.dialog || b) {
                e = null
            } else {
                g$.sound.play(e, -1)
            }
        },
        bgCheck: function (f) {
            if (!g$.sound.bgSound.length) {
                return
            }
            if (a) {
                a = clearTimeout(a)
            }
            a = setTimeout(function () {
                if (g$.popup.dialog || b) {
                    e = g$.sound.stop(e) && null
                } else {
                    if (!e) {
                        g$.sound.play(e = g$.sound.bgSound.randItem(), -1)
                    }
                }
                a = 0
            }, f || 0)
        },
        bgSet: function (f) {
            g$.sound.clear();
            g$.sound.bgSound = f || [];
            g$.sound.bgCheck(0);
            return g$.sound
        }
    };
    g$.popup.onClose.push(g$.sound.bgCheck)
})();
g$.Sprite = function (a, e, f) {
    f = f || e;
    var i = this,
        g, c, b, h, d = [];
    this.img = new Image();
    this.img.onload = function () {
        b = i.img.width;
        g = b / f;
        h = i.img.height;
        c = h * f / e;
        var j = d;
        d = null;
        while (j.length) {
            var k = j.shift();
            i.set(k.el, k.sc, k.sr)
        }
        d = null
    };
    this.img.src = a;
    this.set = function (m, p, l) {
        var k = $(m);
        m = k[0];
        if (!m.sprite) {
            m.sprite = {}
        }
        if (d) {
            d.push({
                el: m,
                sc: p,
                sr: l
            });
            return
        }
        var j = p;
        if (!isNaN(l)) {
            j += l * f
        }
        l = Math.floor((j = j % e) / f);
        p = j % f;
        if (!m.pos) {
            m.pos = g$.ElemPos(m)
        }
        if (!m.pos.w) {
            m.pos.w = k.width() || g
        }
        if (!m.pos.h) {
            m.pos.h = k.height() || c
        }
        var o = m.pos.w / g,
            n = m.pos.h / c;
        if (m.sprite.src != a) {
            m.sprite = {
                src: a,
                sc: -1,
                sr: -1,
                rx: -1,
                ry: -1
            };
            k.css({
                backgroundImage: "url('" + a + "')",
                backgroundRepeat: "no-repeat",
                overflow: "hidden"
            })
        }
        if (m.sprite.rx != o || m.sprite.ry != n) {
            m.style[g$.stylePrefix + "BackgroundSize"] = m.style.backgroundSize = b * (m.sprite.rx = o) + " " + h * (m.sprite.ry = n)
        }
        if (m.sprite.sr != l || m.sprite.sc != p) {
            m.style.backgroundPosition = "-" + ((m.sprite.sc = p) * g * o + (p ? 0 : 0)) + " -" + ((m.sprite.sr = l) * c * n + (l ? 0 : 0))
        }
        m.sprite.idx = j
    };
    return this
};
g$.ElemPos = function (b) {
    if (isNaN(b.length)) {
        b = [b]
    }
    if (b.length && !b[0].style) {
        return b[0].pos
    }
    for (var a = b.length - 1; a >= 0; a--) {
        b[a].pos = {
            x: parseInt(b[a].style.left) || 0,
            y: parseInt(b[a].style.top) || 0,
            w: parseInt(b[a].style.width),
            h: parseInt(b[a].style.height),
            sx: 0,
            sy: 0
        }
    }
    return b[0].pos
};
(function (d) {
    var c = 0;
    g$.score = {
        defAsc: false,
        strName: "oscore",
        defMsg: "High Score"
    };
    var b = null,
        a = 0;
    g$.score.add = function (e, i, k) {
        a = i;
        e = d(e);
        if (!b) {
            b = d("<div/><div/><div/>").css({
                position: "absolute",
                color: "#ff0000",
                fontWeight: 900,
                fontSize: 20,
                display: "block"
            }).hide().appendTo(g$.body()).toArray()
        }
        if (!b.length) {
            e.text(num2str(a));
            return
        }
        var j = d(k).offset(),
            f = e.offset();
        var h = num2str(a - parseInt("" + e.text().replace(/[, ]/g, "")));
        var g = d(b.pop()).removeClass("addScore").css({
            top: j.top,
            left: j.left,
            opacity: ""
        }).text("[" + h + "pts]").show();
        g.cssAnimate({
            top: f.top,
            left: f.left,
            opacity: 0.1
        }, 2500, function () {
            if (!g$.popup.dialog) {
                e.text(num2str(a))
            }
            b.push(g.hide()[0])
        })
    };
    g$.score.save = function (g, h) {
        var e = g$.store.getObj(g$.score.strName, []);
        if (!d.isArray(e)) {
            e = []
        }
        if (!g) {
            return e
        }
        var f = e.indexOf(c);
        if (f >= 0) {
            e.splice(f, 1)
        }
        e.push(Math.round(g));
        c = g;
        e.sort(function (j, i) {
            return h ? j - i : i - j
        });
        e.splice(9, 999);
        g$.store.set(g$.score.strName, JSON.stringify(e));
        return e
    };
    g$.score.makeGUI = function (g, e) {
        var f = "<table>",
            h = "";
        d.each(e, function (m, l) {
            var k = "";
            if (!h && l == g) {
                h = k = "background-color:silver; color:red; font-weight:900;"
            }
            if (l) {
                f += '<tr><td width=50 style="{2}">{0}.</td><td width=150 align=right style="{2}">{1}</td></tr>'.format(m + 1, num2str(l), k)
            }
        });
        return f + "</table>"
    };
    g$.score.show = function (j, g, h, i) {
        var e = g$.score.save(g, h);
        var f = function () {
            (g && !i) ? g$.popup.close(): g$.help.menu()
        };
        if (g && i) {
            c = 0
        }
        g$.popup.show({
            title: j || g$.score.defMsg,
            msg: g$.score.makeGUI(g, e),
            b1Text: "OK",
            b1Click: f,
            b2Text: "Reset",
            b2Click: function () {
                g$.popup.show({
                    msg: "Reset score?",
                    b1Text: "OK",
                    b1Click: function () {
                        g$.store.set(g$.score.strName, "");
                        f()
                    },
                    b2Text: "Cancel",
                    b2Click: f
                })
            }
        })
    };
    g$.score.reset = function () {
        return c = 0
    };
    g$.score.display = function (e) {
        return g$.score.show(null, parseInt(e) || 0, g$.score.defAsc, true)
    };
    g$.menu = {
        cur: null,
        stack: [],
        back: function () {
            if (!g$.popup.dialog) {
                return
            }
            if (g$.menu.stack.length > 0) {
                g$.help.menu(g$.menu.stack.pop())
            } else {
                g$.popup.close()
            }
        }
    };
    d.score = g$.score
})(jQuery);

function WinLossStats() {
    var a = {
        cp: 0,
        cw: 0
    };
    return {
        defMsg: "Statistics",
        update: function (b) {
            var c = g$.store.getObj(g$.score.strName, a);
            c.cw += b;
            c.cp++;
            g$.store.set(g$.score.strName, JSON.stringify(c))
        },
        save: function () {
            return g$.store.getObj(g$.score.strName, a)
        },
        makeGUI: function (c, b) {
            return ("<table style='width:240px' border=1><tr><th>Played</th>	<td align=right>{0}</td></tr><tr><th>Won</th>		<td align=right>{1}</td></tr><tr><th>%</th>		<td align=right>{2}</td></tr></table>").format(b.cp, b.cw, Math.round(b.cw * 100 / (b.cp || 1)))
        }
    }
}(function () {
    var b = "";
    var a = null,
        c = null;
    g$.help = {
        show: function (d) {
            g$.popup.show({
                msg: d || b,
                b1Text: "OK",
                b1Click: function () {
                    g$.help.menu()
                }
            })
        },
        welcome: function (d) {
            b = d;
            if (!g$.store.getString("Welcome", "")) {
                g$.popup.show({
                    msg: b,
                    b1Text: "OK",
                    tmo: 5000
                });
                g$.store.set("Welcome", "OK")
            }
        },
        menu: function (h) {
            var f = function (k, i) {
                return typeof (k) == "function" ? k(i) : k
            };
            if (!a) {
                a = h;
                window.setTimeout(function () {
                    g$.help.menu()
                }, 50);
                return
            }
            if (!h) {
                g$.menu.stack = []
            }
            c = h || a;
            h = f(c);
            if (!h) {
                return g$.popup.close()
            }
            var d = $("<div/>").css({
                padding: "3px 0 0 0",
                display: "inline-block",
                width: (g$.WinWidth > g$.WinHeight ? 420 : 207)
            });
            var j = function (k, n, l) {
                var m = c,
                    i = g$.popup.uid;
                k(n, l);
                if (i == g$.popup.uid || !g$.popup.dialog) {
                    return 0
                }
                if (c != a) {
                    g$.menu.stack.push(m)
                }
                return 1
            };
            var e = function (u) {
                var k = (u.length) ? {
                    text: u[0],
                    click: u[1],
                    prm: u[2],
                    type: ""
                } : f(u);
                if (!k) {
                    return
                }
                if (!k.change) {
                    k.change = k.click
                }
                var n = f(k.val || k.text),
                    p = f(k.text || k.val || ""),
                    i = f(k.list),
                    l = 0;
                if (n === null) {
                    return
                }
                if (!k.type && i) {
                    k.type = "list"
                }
                var s = $("<span class='round button shadow' /> ").appendTo(d).css({
                    width: 200,
                    fontSize: 20,
                    margin: "0px 3px 5px",
                    whiteSpace: "nowrap",
                    position: "relative",
                    padding: "10px 0",
                    display: "inline-block"
                });
                var q = $("<span/>").text("" + p).appendTo(s);
                var v = {
                    position: "relative",
                    width: 40,
                    textAlign: "center"
                };
                if (k.type == "check") {
                    l = (n = f(k.val)) == i[0] ? 0 : 1;
                    var t = $("<img style='left:160px; top:4px;' />").attr("src", l ? "inc/cancel.png" : "inc/ok.png").css({
                        position: "absolute",
                        width: 30,
                        height: 30
                    }).appendTo(s).bind(g$.touchDown, function () {
                        t.attr("src", (l = ~~!l) ? "inc/cancel.png" : "inc/ok.png");
                        j(k.change, i[l])
                    });
                    q.bind(g$.touchDown, function () {
                        if (k.click == k.change) {
                            t.attr("src", (l = ~~!l) ? "inc/cancel.png" : "inc/ok.png")
                        }
                        if (!j(k.click, i[l]) && k.click == k.change) {
                            g$.popup.close()
                        }
                    })
                } else {
                    if (k.type == "list") {
                        n = f(k.val);
                        var r = i;
                        if (typeof (i) == "object" && !$.isArray(i)) {
                            r = [];
                            $.each(i, function (w, m) {
                                r.push(w);
                                r.push(w)
                            })
                        }
                        for (l = r.length - 2; l >= 0 && r[l] != n; l -= 2) {}
                        var o = function (m) {
                            q.html(r[1 + (l = (r.length + l + m) % r.length)])
                        };
                        o(0);
                        $("<div style='float:right;'>&raquo;</div>").prependTo(s).css(v).bind(g$.touchDown, function (m) {
                            m.preventDefault();
                            o(2), j(k.change, r[l])
                        });
                        $("<div style='float:left;'>&laquo;</div>").prependTo(s).css(v).bind(g$.touchDown, function (m) {
                            m.preventDefault();
                            o(-2), j(k.change, r[l])
                        });
                        q.bind(g$.touchDown, function () {
                            if (!j(k.click, r[l]) && k.click == k.change) {
                                g$.popup.close()
                            }
                        })
                    } else {
                        s.bind(g$.touchDown, function (m) {
                            if (!j(k.click, f(k.prm))) {
                                g$.popup.close()
                            }
                        })
                    }
                }
            };
            for (var g = 0; g < h.length; g++) {
                if (h[g]) {
                    e(h[g])
                }
            }
            g$.popup.show({
                msg: d,
                tmo: 16777215
            })
        },
        menuToggle: function () {
            g$.popup.dialog ? g$.popup.close() : g$.help.menu()
        },
        moreGames: function () {
            g$.help.menu();
            if (window.Score) {
                window.Score.MoreGames()
            } else {
                window.location.href = "../../welcome.html"
            }
        },
        menuBack: function () {
            if (g$.popup.dialog) {
                g$.menu.back()
            } else {
                if (window.Score) {
                    window.Score.Quit()
                }
            }
        },
        menuMarket: function (d) {
            return ["More Games", $.help.moreGames]
        }
    };
    $.help = g$.help
})();
$(document).keydown(function (a) {
    if (a.keyCode == 93 || a.keyCode == 36 || a.keyCode == 27) {
        g$.help.menuToggle()
    } else {
        if (a.keyCode == 8 && g$.popup.dialog) {
            g$.menu.back()
        } else {
            return true
        }
    }
    a.preventDefault();
    a.stopPropagation();
    return false
});

function ShowScore(d, c, b, a) {
    g$.score.show(d, c, a, b)
}
jQuery.extend(jQuery.easing, {
    easeLinear: function (c, d, a, b) {
        return a + b * c
    },
    easeThrow: function (e, f, a, i, h) {
        var g = 0;
        if ((f /= h) < (1 / 2.75)) {
            g = i * (9.5625 * (f -= 0.1) * f - 0.1) + a
        } else {
            if (f < (2 / 2.75)) {
                g = i * (7.5625 * (f -= (1.5 / 2.75)) * f + 0.75) + a
            } else {
                if (f < (2.5 / 2.75)) {
                    g = i * (7.5625 * (f -= (2.25 / 2.75)) * f + 0.9375) + a
                } else {
                    g = i * (7.5625 * (f -= (2.625 / 2.75)) * f + 0.984375) + a
                }
            }
        }
        return g
    },
    easeOutElastic: function (f, h, e, l, k) {
        var i = 1.70158,
            j = 0,
            g = l;
        if (h == 0) {
            return e
        }
        if ((h /= k) == 1) {
            return e + l
        }
        if (!j) {
            j = k * 0.3
        }
        if (g < Math.abs(l)) {
            g = l;
            var i = j / 4
        } else {
            var i = j / (2 * Math.PI) * Math.asin(l / g)
        }
        return g * Math.pow(2, -10 * h) * Math.sin((h * k - i) * (2 * Math.PI) / j) + l + e
    }
});
jQuery.fx.interval = 40;
(function () {
    var d = [],
        c = 0,
        a = 0,
        b = {
            bounce: "cubic-bezier(0.0, 0.35, .5, 1.3)",
            linear: "linear",
            swing: "ease-in-out"
        };

    function e() {
        var h = Date.now(),
            f = h + 999999;
        for (var g = d.length - 1; g >= 0; g--) {
            var j = d[g];
            f = Math.min(f, j.dur);
            if (!j.flg) {
                j.flg = $(j.el).css(g$.extCss(j.css, j.ext))
            }
            if (j.dur > h) {
                continue
            }
            d.splice(g, 1);
            $(j.el).css(g$.extCss({}, {
                TransitionProperty: "",
                TransitionDuration: "",
                TransitionTimingFunction: ""
            }));
            if (jQuery.isFunction(j.done)) {
                j.done.apply(j.el)
            }
        }
        if (c) {
            c = clearTimeout(c)
        }
        if (a) {
            a = clearTimeout(a)
        }
        if (d.length) {
            a = setTimeout(function () {
                e(a = 0)
            }, Math.max(f - Date.now(), 0) + 10)
        }
    }
    jQuery.fn.cssAnimate = function (u, h, t, v) {
        if (!g$.cssTransitions) {
            return this.animate(u, h, t, v)
        }
        var f = jQuery.speed(h, t, v),
            q = {},
            k = [],
            o = [],
            p = [];
        if (jQuery.isEmptyObject(u)) {
            return this.each(f.complete)
        }
        f.easing = b[f.easing || "swing"] || f.easing;
        for (var g in u) {
            k.push(g), p.push(h);
            if ($.isArray(u[g])) {
                q[g] = u[g][0];
                o.push(b[u[g][1] || "swing"] || f.easing)
            } else {
                q[g] = u[g];
                o.push(f.easing)
            }
        }
        for (var l = this.length - 1; l >= 0; l--) {
            var r = this[l],
                m = -1;
            for (m = 0; m < d.length && d[m].el != r; m++) {}
            if (m < d.length && jQuery.isFunction(d[m].done)) {
                var s = d[m].done;
                d[m].done = null;
                s.apply(r)
            }
            d[m] = {
                el: r,
                css: q,
                done: f.complete,
                flg: 0,
                dur: (new Date()).getTime() + f.duration,
                ext: {
                    TransitionProperty: k.join(", "),
                    TransitionDuration: f.duration + "ms",
                    TransitionTimingFunction: o.join(", ")
                }
            }
        }
        if (!c) {
            c = setTimeout(function () {
                e(c = 0)
            }, 1)
        }
        return this
    }
})();
g$.fxRemove = function (f, e, h, b, j) {
    if (!(f = $(f)).length) {
        return
    }
    var d = 0,
        c = $(f[0]),
        g = c.parent(),
        a = {
            w: g.width(),
            h: g.height()
        },
        k = {
            w: c.width(),
            h: c.height()
        };
    if (typeof (e) == "number") {
        j = h;
        h = e;
        e = (h > 1) ? "inc/bang.png" : null;
        b = (h == 1) ? 1000 : 700
    }
    $(f).each(function () {
        var m = $(this),
            l = m.position();
        if (e) {
            m.attr("src", e)
        }
        if (!j) {
            m.css({
                pointerEvents: "none"
            })
        }
        m.css({
            position: "absolute",
            zIndex: 9000
        });
        var i = (e || h != 1) ? {
            left: l.left + k.w * (1 - h) / 2,
            width: k.w * h,
            top: l.top + k.h * (1 - h) / 2,
            height: k.h * h,
            opacity: 0
        } : {
            left: [0 + (Math.random() < 0.5 ? 0 : a.w - k.w), "linear"],
            top: [0 + a.h - k.h, "easeThrow"]
        };
        if (e || h != 1) {
            m.cssAnimate(i, b, function () {
                if (!j) {
                    m.remove()
                } else {
                    if (!d++) {
                        j(m)
                    }
                }
            })
        } else {
            m.animate(i, b, function () {
                if (!j) {
                    m.remove()
                } else {
                    if (!d++) {
                        j(m)
                    }
                }
            })
        }
    })
};
g$.Delayed = function () {
    var c = this,
        b = 0;
    c.qu = [];

    function a() {
        if (c.qu.length == 0) {
            return
        }
        var g = c.qu[0],
            h = g.cnt || 1,
            d = null;
        if (typeof (h) == "function") {
            h = h()
        }
        if (!isNaN(h.length)) {
            d = h[b];
            h = h.length
        }
        if (b < h) {
            g.fn(b++, d)
        }
        if (b >= h) {
            if (h == 1 && c.qu.length == 1) {
                g.cnt = -1
            } else {
                b = 0, c.qu.shift()
            }
        }
        if (c.qu.length == 0) {
            return
        }
        var f = c.qu[0].tmo;
        if (f.length) {
            f = Math.rand(f[0], f[1]) * (f[2] || 1)
        }
        if (f) {
            setTimeout(a, f)
        } else {
            a()
        }
    }
    c.add = function (f, e, d) {
        c.qu.push({
            fn: f,
            tmo: (e || 200),
            cnt: (d || 1)
        });
        if (c.qu.length == 1) {
            a()
        }
        return c
    };
    c.clean = function () {
        b = 0;
        c.qu = [];
        return c
    };
    return c
};
g$.DelayMgr = g$.Delayed();

function ShowMarker(d, c, g, h) {
    var b = c,
        f = g;
    d = $(d);
    g$.ElemPos(d);
    var a = d[0].pos.h / 2;
    if (c.nodeType || c.jquery) {
        b = g$.ElemPos(c)
    }
    if (g.nodeType || g.jquery) {
        f = g$.ElemPos(g)
    }
    f.h = f.h || 1;
    f.w = f.w || 1;
    f.x += f.w / 2 - 1 * a;
    b.x += b.w / 2 - 1 * a;
    f.y += f.h / 2 - 1 * a;
    b.y += b.h / 2 - 1 * a;
    g$.extCss(d[0].style, {
        TransformOrigin: a + "px " + a + "px"
    });
    d.css({
        left: b.x,
        top: b.y
    });
    off = {
        x: f.x - b.x,
        y: f.y - b.y,
        len: h
    };
    off.len = Math.min(Math.sqrt(off.x * off.x + off.y * off.y) + a, h || 999);
    var e = function () {
        d.css({
            width: off.len,
            rotate: Math.atan2(off.y, off.x) / Math.PI * 180
        })
    };
    if (!h) {
        if (Math.pow(d[0].pos.x - b.x, 2) + Math.pow(d[0].pos.y - b.y, 2) > a * a) {
            d.css({
                width: 1
            })
        }
        window.setTimeout(e, 1)
    } else {
        e()
    }
    return off
}

function TitleBar(b) {
    var j = $("<div class='button' style='position:absolute; top:0px; left:0; font-size:12pt; z-index:999991; text-align:left; padding:0;'>&nbsp;</div>").css({
        width: g$.WinWidth,
        height: 25
    }).appendTo(g$.body());
    var f = document.title;
    var e = $('<div class="progress"/>').css({
        position: "absolute",
        top: 0,
        left: 0,
        height: 25,
        width: 0,
        zIndex: -1
    }).appendTo(j);
    var a = $("<span style='position:absolute; left:0px; top:0px; width:100%; height:25px; white-space:nowrap; overflow:hidden; line-height:25px'/>").html(f).appendTo(j);
    var c = $("<span style='float:right; position:relative; z-index:1;' />").appendTo(j);
    if (!b) {
        b = []
    }
    b.push("inc/menu.png");
    b.push(g$.help.menuToggle);
    var h = [];
    for (var d = 0; d < b.length; d++) {
        var g = b[d];
        if (typeof (g) == "function") {
            $(h.last()).bind(g$.touchDown, g);
            continue
        }
        if (g.indexOf(".png") > 0) {
            h.push($("<img style='height:23px; vertical-align:middle; padding:0 5px' />").attr("src", b[d]).appendTo(c)[0])
        } else {
            h.push($("<span style='padding:0 5px; line-height:25px;' />").html(g).appendTo(c)[0])
        }
    }
    this.message = function (k, l) {
        if (!l) {
            a.html(f = k);
            return
        }
        var n = ["red", "white"],
            i = [k, f];
        g$.DelayMgr.add(function (m) {
            a.css("color", n[m]).html(i[m])
        }, l, 2);
        return g$.TitleBar
    };
    this.pane = function (k, i) {
        $(h[k]).html("" + i);
        return g$.TitleBar
    };
    this.score = function (k, i, l) {
        g$.score.add(h[k], i, l);
        return g$.TitleBar
    };
    this.progress = function (i) {
        e.css({
            width: 100 * Math.range(0, i, 1) + "%"
        });
        return g$.TitleBar
    };
    this.lives = function (m, n) {
        var l = "",
            k = parseInt(n);
        while (k-- > 0) {
            l += "&hearts;"
        }
        $(h[m]).css("color", "red").html(l);
        return g$.TitleBar
    };
    this.focus = function () {
        j.css({
            zIndex: g$.popup.dialog ? 999991 : 0
        })
    };
    this.container = j;
    return g$.TitleBar = this
}(function (a) {
    function c(g, j, d) {
        var f = a.data(g, "xform") || {},
            e = "",
            i = f[j],
            h = (j == "scale") ? 10000 : 1;
        if (~~(d * h) == ~~(i * h)) {
            return
        }
        f[j] = d;
        a.data(g, "xform", f);
        if ((f.scale || 1) != 1) {
            //todo Raymond - to improve chrome's performance
            //e += "scale(" + f.scale + "," + f.scale + ") "
            var is_chrome = /chrome/i.test(navigator.userAgent);
            if (is_chrome) {
                e += "scale3d(" + f.scale + "," + f.scale + "," + f.scale + ") ";
            }
            else {
                e += "scale(" + f.scale + "," + f.scale + ") ";
            }
        }
        if (f.rotate) {
            e += " rotate(" + (f.rotate % 360) + "deg)"
        }
        g$.extCss(g.style, {
            Transform: e
        });
        if (f.xferOrgX || f.xferOrgY) {
            g$.extCss(g.style, {
                TransformOrigin: ~~f.xferOrgX + "px " + ~~f.xferOrgY + "px"
            })
        }
    }

    function b(d, e) {
        a.cssNumber[d] = true;
        a.cssHooks[d] = {
            set: function (g, f) {
                c(g, d, parseFloat(f) || e)
            },
            get: function (g, h) {
                var f = a.data(g, "xform");
                return f && f[d] ? f[d] || e : e
            }
        };
        a.fx.step[d] = function (f) {
            a.cssHooks[d].set(f.elem, f.now + f.unit)
        }
    }
    b("scale", 1);
    b("rotate", 0);
    b("xferOrgX", 0);
    b("xferOrgY", 0);
    a.cssHooks.rotate.set = function (e, d) {
        if (typeof d === "string") {
            d = (d.indexOf("rad") != -1) ? parseInt(d) * 180 / Math.PI : parseInt(d)
        }
        c(e, "rotate", ~~parseInt(d))
    }
})(jQuery);