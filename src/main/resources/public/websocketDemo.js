//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + "4567" + "/chat");
webSocket.onmessage = function (msg) {
  update(msg);
};
webSocket.onclose = function () {
  alert("WebSocket connection closed")
};
var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
canvas.addEventListener('click', function(e) {
  var pos = getMousePos(canvas, e);
  webSocket.send(JSON.stringify(pos));
  draw(pos)
}, false);

setInterval(myMethod, 10);

var frame = 0;
function myMethod() {
  var ctx = canvas.getContext("2d");
  ctx.font = "30px Arial";
  context.clearRect(0, 0, canvas.width, canvas.height);
  ctx.fillText(frame.toString(), 10, 50);
  ctx.fillText("hiooo", 10, 150);
  frame++;
}


function update(msg) {
  var data = JSON.parse(msg.data);
  if (data.point) {
    draw(data.point)
  }
  else {
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
  context.fillStyle = "#010101";
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
