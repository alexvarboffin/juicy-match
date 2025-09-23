var global_canvas_width = 430;//450
var global_square_width;
var global_margin_width = 10;
var global_block_array = [];
var global_startX;
var global_startY;
var global_default_color = "#008DB1";
var global_highlight_color = "red";

var global_index = -1;
var global_square_number; //4x4
var global_enable_touch = true;

//var global_img = new Image();
//global_img.src = "assets/image/block.jpg";

var canvas;
var ctx;
var canvasOffset;
var offsetX;
var offsetY;
var mouseX;
var mouseY;
var isMouseDown = false;

var canvasController = (function () {

    function pointerEventToXY(e) {
        var out = { x: 0, y: 0 };
        if (e.type == 'touchstart' || e.type == 'touchmove' || e.type == 'touchend' || e.type == 'touchcancel') {
            var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
            out.x = touch.pageX;
            out.y = touch.pageY;
        } else if (e.type == 'mousedown' || e.type == 'mouseup' || e.type == 'mousemove' || e.type == 'mouseover' ||
            e.type == 'mouseout' || e.type == 'mouseenter' || e.type == 'mouseleave') {
            out.x = e.pageX;
            out.y = e.pageY;
        }
        return out;
    };

    function shuffle(array) {
        var currentIndex = array.length, temporaryValue, randomIndex;

        // While there remain elements to shuffle...
        while (0 !== currentIndex) {

            // Pick a remaining element...
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex -= 1;

            // And swap it with the current element.
            temporaryValue = array[currentIndex];
            array[currentIndex] = array[randomIndex];
            array[randomIndex] = temporaryValue;
        }

        return array;
    }

    function intersects(rect1, rect2) {
        return !(rect2.x >= (rect1.x + rect1.w) ||
                 (rect2.x + rect2.w) <= (rect1.x) ||
                  rect2.y >= (rect1.y + rect1.h) ||
                 (rect2.y + rect2.h) <= rect1.y);
    }

    //function intersect(a, b) {
    //    return (a.x < (b.x + b.w) &&
    //            b.x < (a.x + a.w) &&
    //            a.y < (b.y + b.h) &&
    //            b.y < (a.y + a.h))
    //}

    function calDistinceBetweenPoints(x1, y1, x2, y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    function drawRectangle(x, y, w, h, color, text) {
        // The painting properties 
        // Normally I would write this as an input parameter
        var Paint = {
            RECTANGLE_STROKE_STYLE: 'black',
            RECTANGLE_LINE_WIDTH: 1,
            VALUE_FONT: '52px Arial',
            VALUE_FILL_STYLE: color
        }

        // draw rectangular
        ctx.strokeStyle = Paint.RECTANGLE_STROKE_STYLE;
        ctx.lineWidth = Paint.RECTANGLE_LINE_WIDTH;
        ctx.strokeRect(x, y, w, h);
        ctx.fillStyle = Paint.VALUE_FILL_STYLE;
        ctx.fillRect(x, y, w, h);

        // draw text (this.val)
        ctx.textBaseline = "middle";
        ctx.font = Paint.VALUE_FONT;
        ctx.fillStyle = global_default_color;
        // ctx2d.measureText(text).width/2 
        // returns the text width (given the supplied font) / 2
        textX = x + w / 2 - ctx.measureText(text).width / 2;
        textY = y + h / 2;
        ctx.fillText(text, textX, textY);
    }

    

    function init() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        global_block_array = [];

        isMouseDown = false;
        global_enable_touch = true;

        //var data = global_unblockme_data[4];
        var data = global_unblockme_data[global_current_level];
        global_square_number = Math.max(parseInt(data["h"]), parseInt(data["w"]));

        global_square_width = global_canvas_width / global_square_number;

        //prepare blocks
        for (var i = 0; i < data["b"].length; i++) {
            var obj = {
                x: global_square_width * (data["b"][i].x == null ? 0 : parseInt(data["b"][i].x)) + global_margin_width,
                //y: global_square_width * (splitNumber - data["b"][i].y == null ? 0 : parseInt(data["b"][i].y)) + global_margin_width,
                y: global_square_width * (global_square_number - parseInt(data["b"][i].h) - (data["b"][i].y == null ? 0 : parseInt(data["b"][i].y))) + global_margin_width,
                w: global_square_width * parseInt(data["b"][i].w),
                h: global_square_width * parseInt(data["b"][i].h),
                c: i == 0 ? global_highlight_color : global_default_color
            };

            global_block_array.push(obj);
        }

        drawBackground();
        drawBlocks();
        drawExit();
    }

    function drawBackground()
    {
        drawRectangle(global_margin_width, global_margin_width, global_canvas_width, global_canvas_width, "white", "");
    }

    function drawBlocks()
    {
        for (var i = 0; i < global_block_array.length; i++) {
            drawRectangle(global_block_array[i].x,
                global_block_array[i].y,
                global_block_array[i].w,
                global_block_array[i].h,
                global_block_array[i].c, "");
        }
    }

    function drawExit()
    {
        var data = global_unblockme_data[global_current_level];
        //var data = global_unblockme_data[4];

        drawRectangle(
            global_square_width * parseInt(data["e"]["x"]) + global_margin_width,
            global_square_width * (global_square_number - parseInt(data["e"]["y"]) - 1) + global_margin_width,
            global_margin_width,
            global_square_width,
            global_highlight_color, "");
    }

    function selectedBlockIndex(e)
    {
        mouseX = parseInt(pointerEventToXY(e).x / g$.bodyScale - offsetX);
        mouseY = parseInt(pointerEventToXY(e).y / g$.bodyScale - offsetY);

        var returnValue = -1;
        for (var i = 0; i < global_block_array.length; i++)
        {
            if ((mouseX > global_block_array[i].x) &&
                (mouseX < global_block_array[i].x + global_block_array[i].w) &&
                (mouseY > global_block_array[i].y) &&
                (mouseY < global_block_array[i].y + global_block_array[i].h)) {
                returnValue = i;
                soundController.select();
                break;
            }
        }

        global_index = returnValue;
    }

    function drawMovement(e)
    {
        if (global_index == -1)
            return;

        var newMouseX = parseInt(pointerEventToXY(e).x / g$.bodyScale - offsetX);
        var newMouseY = parseInt(pointerEventToXY(e).y / g$.bodyScale - offsetY);

        var selectedBlock = global_block_array[global_index];

        //check overlapping
        //to test
        //for (var i = 0; i < global_block_array.length; i++) {
        //    if (i != global_index)
        //    {
        //        var isOverlapping = intersects(selectedBlock, global_block_array[i]);
        //        if (isOverlapping) {
        //            return;
        //        }
        //    }
        //}

        var ox = selectedBlock.x;
        var oy = selectedBlock.y;

        if (selectedBlock.w > selectedBlock.h) {
            // Left - Right - MouseX
            
            selectedBlock.x = selectedBlock.x + (newMouseX - mouseX);

            selectedBlock.x = Math.max(global_margin_width, selectedBlock.x);

            selectedBlock.x = Math.min(global_canvas_width - selectedBlock.w + global_margin_width, selectedBlock.x);

            mouseX = newMouseX;
        }
        else {
            // Up - Down - MouseY
            selectedBlock.y = selectedBlock.y + (newMouseY - mouseY);

            selectedBlock.y = Math.max(global_margin_width, selectedBlock.y);

            selectedBlock.y = Math.min(global_canvas_width - selectedBlock.h + global_margin_width, selectedBlock.y);

            mouseY = newMouseY;
        }

        //check overlapping
        //to test
        for (var i = 0; i < global_block_array.length; i++) {
            if (i != global_index) {
                var isOverlapping = intersects(selectedBlock, global_block_array[i]);
                if (isOverlapping) {
                    selectedBlock.x = ox;
                    selectedBlock.y = oy;
                    return;
                }
            }
        }

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        drawBackground();
        drawBlocks();
        drawExit();
    }

    function adjustSelectedBlock() {
        if (global_index == -1)
            return;

        var selectedBlock = global_block_array[global_index];
        var adjustX, adjustY;
        var shortestDist = 1000000;
        for (var i = 0; i < global_square_number; i++) {
            for (var j = 0; j < global_square_number; j++) {
                var x = global_square_width * i + global_margin_width;
                var y = global_square_width * j + global_margin_width;

                var dist = calDistinceBetweenPoints(selectedBlock.x, selectedBlock.y, x, y);
                if (dist < shortestDist) {
                    shortestDist = dist;
                    adjustX = x;
                    adjustY = y;
                }
            }
        }

        selectedBlock.x = adjustX;
        selectedBlock.y = adjustY;

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        drawBackground();
        drawBlocks();
        drawExit();
    }

    function checkWin() {

        var redBlock = global_block_array[0];

        var data = global_unblockme_data[global_current_level];
        //var data = global_unblockme_data[4];

        if ((redBlock.x + redBlock.w) == (global_square_width * parseInt(data["e"]["x"]) + global_margin_width)) {

            global_enable_touch = false;

            soundController.move();
            

            var maxLevel = scoreController.getLevel();
            var nextLevel = parseInt(global_current_level) + 2;

            setTimeout(function () {

                soundController.solved();

                if (nextLevel > maxLevel) {
                    scoreController.setLevel(nextLevel);

                    //todo
                    syncGameScore("Unblock Me", nextLevel, scoreController.setLevel);

                    pageController.renderLevels();

                    pageController.renderMessage("Congratulations! You Win!");
                }
                else {
                    pageController.renderMessage("Congratulations! You Win!");
                }

                //if ((nextLevel - 1) % 3 == 0) {
                    //show ads
                    try { showXpAds(); } catch (err) { }
                //}

            }, 1000);

            winAutoMoving();

            if (nextLevel <= global_unblockme_data.length) {
                setTimeout(function () { $("#popupMessage").modal("hide"); pageController.chooseLevel(nextLevel); }, 2300);
            } else {
                setTimeout(function () { $("#popupMessage").modal("hide"); $("#levelModal").modal("show"); }, 2300);
            }

        }
    }

    function winAutoMoving() {
        var redBlock = global_block_array[0];

        var dist = redBlock.w / 5;

        var times = 1;
        var intervalID = setInterval(function () {

            // Your logic here
            redBlock.x = redBlock.x + dist;

            ctx.clearRect(0, 0, canvas.width, canvas.height);
            drawBackground();
            drawBlocks();
            drawExit();

            if (times++ === 5) {
                window.clearInterval(intervalID);
            }
        }, 100);
    }

    function handleMouseDown(e) {
        if (!global_enable_touch) return;

        isMouseDown = true;
        selectedBlockIndex(e);
    }

    function handleMouseUp(e) {
        if (!global_enable_touch) return;

        isMouseDown = false;

        adjustSelectedBlock();
        checkWin();
    }

    function handleMouseMove(e) {
        if (!global_enable_touch) return;

        if (isMouseDown)
        {
             drawMovement(e);
        }
    }
    
    return {
        handleMouseDown: handleMouseDown,
        handleMouseUp: handleMouseUp,
        handleMouseMove: handleMouseMove,
        pointerEventToXY: pointerEventToXY,
        checkWin: checkWin,
        drawRectangle: drawRectangle,
        init: init,
        selectedBlockIndex: selectedBlockIndex,
        drawMovement: drawMovement
    };
}());


$(function (e) {

    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    canvasOffset = $("#canvas").offset();
    //offsetX = canvasOffset.left;
    offsetX = canvasOffset.left + g$.bodyOffset / g$.bodyScale;
    offsetY = canvasOffset.top;

    g$.touchDown = "touchstart mousedown";
    g$.touchMove = "touchmove mousemove";
    g$.touchUp = "touchend mouseup";
    g$.touchOut = "touchcancel mouseout";

    //canvasController.restoreGameStatus();
    //canvasController.draw(e);

    //canvasController.init();
    $('#canvas').bind(g$.touchDown, function (e) {
        e.preventDefault();
        canvasController.handleMouseDown(e);
    });

    $('#canvas').bind(g$.touchMove, function (e) {
        e.preventDefault();
        canvasController.handleMouseMove(e);

    });

    $('#canvas').bind(g$.touchUp, function (e) {
        e.preventDefault();
        canvasController.handleMouseUp(e);
    });

    //tocheck
    $('#canvas').bind(g$.touchOut, function (e) {
        e.preventDefault();
        if (isMouseDown)
           canvasController.handleMouseUp(e);
    });
});

