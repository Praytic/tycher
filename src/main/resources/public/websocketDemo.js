//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + "4567" + "/chat");
webSocket.onopen = function () {
  setInterval(update, 1000 / 60);
};
webSocket.onmessage = function (msg) {
  handleMessage(msg);
};
webSocket.onclose = function () {
  alert("WebSocket connection closed")
};

window.addEventListener('keydown', function(event) {
  inputs.push(event.which);
}, false);


var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
canvas.addEventListener('click', function(e) {
  // var pos = getMousePos(canvas, e);
  // webSocket.send(JSON.stringify(pos));
  // draw(pos);
}, false);
var mouseX = 0;
var mouseY = 0;
canvas.addEventListener('mousemove', function(e) {
  var pos = getMousePos(canvas, e);
  mouseX = pos.x;
  mouseY = pos.y;
}, false);


var frame = 0;
var inputs = [0];
var positions = [];
function update() {
  frame++;
  
  // Send inputs to server
  if (inputs.length != 0) {
    webSocket.send(JSON.stringify({
      keys: inputs, 
      mouseX: mouseX, 
      mouseY: mouseY,
    }));
    inputs = [0];
  }

  // Draw users
  for (var i = 0; i < positions.length; i++) {
    context.fillText("a", positions[i * 2], positions[i * 2 + 1]);
  }
}


function handleMessage(msg) {
  var data = JSON.parse(msg.data);
  if (data.positions) {
    positions = data.positions;
  } else {
    insert("chat", data.userMessage);
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
      insert("userlist", "<li>" + user + "</li>");
    });
  }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
  var data = JSON.parse(msg.data);
  insert("chat", data.userMessage);
  id("userlist").innerHTML = "";
  data.userlist.forEach(function (user) {
    insert("userlist", "<li>" + user + "</li>");
  });
}


//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
  id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
  return document.getElementById(id);
}

function draw(pos) {
  posx = pos.x;
  posy = pos.y;
  context.fillStyle = "#000000";
  context.globalAlpha = 0.5;
  context.beginPath();
  context.arc(posx, posy, 50, 0, 2 * Math.PI);
  context.fill();
}

function getMousePos(canvas, evt) {
  var rect = canvas.getBoundingClientRect();
  return {
    x: evt.clientX - rect.left,
    y: evt.clientY - rect.top
  };
}

var Key = {
  NUMBER_0: 48,
  NUMBER_1: 49,
  NUMBER_2: 50,
  NUMBER_3: 51,
  NUMBER_4: 52,
  NUMBER_5: 53,
  NUMBER_6: 54,
  NUMBER_7: 55,
  NUMBER_8: 56, 
  NUMBER_9: 57,
  A: 65,
  B: 66,
  C: 67,
  D: 68,
  E: 69,
  F: 70,
  G: 71,
  H: 72,
  I: 73,
  J: 74,
  K: 75,
  L: 76,
  M: 77,
  N: 78,
  O: 79,
  P: 80,
  Q: 81,
  R: 82,
  S: 83,
  T: 84,
  U: 85,
  V: 86,
  W: 87,
  X: 88,
  Y: 89,
  Z: 90,
  NUMPAD_0: 96,
  NUMPAD_1: 97,
  NUMPAD_2: 98,
  NUMPAD_3: 99,
  NUMPAD_4: 100,
  NUMPAD_5: 101,
  NUMPAD_6: 102,
  NUMPAD_7: 103,
  NUMPAD_8: 104,
  NUMPAD_9: 105,
  NUMPAD_MULTIPLY: 106,
  NUMPAD_ADD: 107,
  NUMPAD_ENTER: 108,
  NUMPAD_SUBTRACT: 109,
  NUMPAD_DECIMAL: 110,
  NUMPAD_DIVIDE: 111,
  F1: 112,
  F2: 113,
  F3: 114,
  F4: 115,
  F5: 116,
  F6: 117,
  F7: 118,
  F8: 119,
  F9: 120,
  F10: 121, //  F10 is used by browser.
  F11: 122,
  F12: 123,
  F13: 124,
  F14: 125,
  F15: 126,
  BACKSPACE: 8,
  TAB: 9,
  ALTERNATE: 18,
  ENTER: 13,
  COMMAND: 15,
  SHIFT: 16,
  CONTROL: 17,
  BREAK: 19,
  CAPS_LOCK: 20,
  NUMPAD: 21,
  ESCAPE: 27,
  SPACE: 32,
  PAGE_UP: 33,
  PAGE_DOWN: 34,
  END: 35,
  HOME: 36,
  LEFT: 37,
  RIGHT: 39,
  UP: 38,
  DOWN: 40,
  INSERT: 45,
  DELETE: 46,
  NUMLOCK: 144,
  SEMICOLON: 186,
  EQUAL: 187,
  COMMA: 188,
  MINUS: 189,
  PERIOD: 190,
  SLASH: 191,
  BACKQUOTE: 192,
  LEFTBRACKET: 219,
  BACKSLASH: 220,
  RIGHTBRACKET: 221,
  QUOTE: 222,
};
