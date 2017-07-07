// Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + "4567" + "/chat");
webSocket.onopen = function () {
};
webSocket.onmessage = function (msg) {
  handleMessage(msg);
};
webSocket.onclose = function () {
  alert("WebSocket connection closed")
};

var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");


window.addEventListener('keydown', function(event) {
  keys[event.which] = 1;
}, false);
var mouseX = 0;
var mouseY = 0;
canvas.addEventListener('click', function(e) {
  var pos = getMousePos(canvas, e);
  click_keys.push(event.which);
  click_positions.push(pos.x);
  click_positions.push(pos.y);
}, false);
canvas.addEventListener('mousemove', function(e) {
  var pos = getMousePos(canvas, e);
  mouseX = pos.x;
  mouseY = pos.y;
}, false);


var frame = 0;
var positions = [];
var keys = Array(223).fill(-1);
var click_keys = Array(4).fill(-1);
var click_positions = [];
var lastTime = (new Date()).getTime();
var delta = 0;
// https://coderwall.com/p/iygcpa/gameloop-the-correct-way
function update() {
  window.requestAnimationFrame(update);
  currentTime = (new Date()).getTime();
  delta = (currentTime - lastTime) / 1000;

  // Send inputs to server
  webSocket.send(JSON.stringify({
    keys: keys, 
    click_keys: click_keys,
    click_positions: click_positions,
    mouseX: mouseX, 
    mouseY: mouseY,
  }));
  keys.fill(-1);
  click_keys.fill(-1);
  click_positions = [];

  // Draw users
  context.clearRect(0, 0, canvas.width, canvas.height);
  context.font = "30px Arial";
  for (var i = 0; i < positions.length; i++) {
    context.fillText("a", positions[i * 2], positions[i * 2 + 1]);
  }

  frame++;
  context.fillText(frame.toString(), 5, 25);

  lastTime = currentTime;
}
// Start loop
update();


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