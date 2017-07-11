//Establish the WebSocket connection and set up event handlers
let game = new WebSocket("ws://" + location.hostname + ":" + "4567" + "/game");
let board = new WebSocket("ws://" + location.hostname + ":" + "4567" + "/board");

game.onmessage = function (msg) {
  let data = JSON.parse(msg.data);
  if (data.scoreboard) {
    id("userlist").innerHTML = "";
    data.scoreboard.forEach(function (entry) {
      insert("userlist", "<li>" + entry.username + " [" + entry.score + "]" + "</li>");
    });
  }
};

let canvas = document.getElementById("myCanvas");
let context = canvas.getContext("2d");

canvas.addEventListener('click', function(e) {
  var pos = getMousePos(canvas, e);
  webSocket.send(JSON.stringify(pos));
  draw(pos)
}, false);

window.onbeforeunload = function() {
  webSocket.close();
  return null;
};

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
