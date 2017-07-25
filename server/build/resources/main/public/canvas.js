const canvas = document.getElementById("myCanvas");
const context = canvas.getContext("2d");

canvas.addEventListener('click', function(e) {
  const pos = getMousePos(canvas, e);
  const tych = {
    position: {
      x: pos.x,
      y: pos.y
    },
    spawnTime: new Date().getTime(),
    dummy: false,
  };
  webSocket.send(JSON.stringify(tych));
}, false);

function drawTych(tych) {
  context.fillStyle = "#000000";
  context.globalAlpha = 0.5;
  context.beginPath();
  context.arc(
      tych.position.x,
      tych.position.y, 50, 0, 2 * Math.PI);
  context.fill();
}

function getMousePos(canvas, evt) {
  var rect = canvas.getBoundingClientRect();
  return {
    x: evt.clientX - rect.left,
    y: evt.clientY - rect.top
  };
}
