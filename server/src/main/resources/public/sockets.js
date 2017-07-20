//Establish the WebSocket connection and set up event handlers
let game = new WebSocket("ws://" + location.hostname + ":" + "4567" + "/game");
let board = new WebSocket("ws://" + location.hostname + ":" + "4567" + "/board");

game.onmessage = function (msg) {
  let data = JSON.parse(msg.data);
  if (data.scoreboard) {
    handleScoreboard(data)
  }
};

board.onmessage = function (msg) {
  let data = JSON.parse(msg.data);
  if (data.spawnFood) {
    handleSpawnFood(data)
  }
  if (data.spawnTychs) {
    handleSpawnTychs(data)
  }
};

function handleSpawnFood(data) {
  // TODO
}

function handleSpawnTychs(data) {
  // TODO
}

function handleScoreboard(data) {
  id("userlist").innerHTML = "";
  data.scoreboard.forEach(function (entry) {
    insert("userlist", `<li>${entry.username} [${entry.score}]</li>`);
  });
}