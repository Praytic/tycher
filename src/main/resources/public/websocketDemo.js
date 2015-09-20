//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + "4567" + "/chat");
webSocket.onmessage = function (msg) {
  updateChat(msg);
};
webSocket.onclose = function () {
  alert("WebSocket connection closed")
};
var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
canvas.addEventListener('click', draw, false);

//Send message if "Send" is clicked
id("send").addEventListener("click", function () {
  sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
  if (e.keyCode === 13) {
    sendMessage(e.target.value);
  }
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
  if (message !== "") {
    webSocket.send(message);
    id("message").value = "";
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

function draw(e) {
  var pos = getMousePos(canvas, e);
  posx = pos.x;
  posy = pos.y;
  context.fillStyle = "#000000";
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
