if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'output'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'output'.");
}
var output = function (_, Kotlin) {
  'use strict';
  var Unit = Kotlin.kotlin.Unit;
  var numberToInt = Kotlin.numberToInt;
  var to = Kotlin.kotlin.to_ujzrz7$;
  var json = Kotlin.kotlin.js.json_pyyo18$;
  var throwCCE = Kotlin.throwCCE;
  var Pair = Kotlin.kotlin.Pair;
  var ensureNotNull = Kotlin.ensureNotNull;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var tychs;
  var scoreboard;
  var canvas;
  var RENDERING_RATIO;
  var UPDATE_RATIO;
  var SCOREBOARD_LIMIT;
  function main$lambda() {
    renderBackground(canvas);
    renderTychs(canvas);
    renderScoreboard(canvas);
    shrinkTychs(canvas);
    return Unit;
  }
  function main$lambda_0() {
    if (gameSocket.readyState === 1) {
      updateScoreboard();
    }
    return Unit;
  }
  function main(args) {
    initWebSockets();
    logWithTime(console, ['Starting game loop...']);
    window.setInterval(main$lambda, numberToInt(RENDERING_RATIO));
    window.setInterval(main$lambda_0, numberToInt(UPDATE_RATIO));
  }
  function updateScoreboard() {
    var scoreboard = json([to('scoreboard', SCOREBOARD_LIMIT)]);
    gameSocket.send(JSON.stringify(scoreboard));
  }
  function handleTych(tych) {
    logWithTime(console, ['Handle tych: (' + tych + ')']);
    var pos = new Position(tych[0], tych[1]);
    var radius = tych[2];
    var shrinkSpeed = tych[3];
    tychs.put_xwzc9p$(pos, new Tych(pos, radius, shrinkSpeed));
  }
  function handleScoreboard$lambda(pair) {
    return scoreboard.put_xwzc9p$(pair[0], pair[1]);
  }
  function handleScoreboard(sb) {
    scoreboard.clear();
    sb.forEach(handleScoreboard$lambda);
  }
  function handleRemoveTych(dtych) {
    logWithTime(console, ['Remove tych: (' + dtych + ')']);
    var pos = new Position(dtych[0], dtych[1]);
    var radius = dtych[2];
    var shrinkSpeed = dtych[3];
    tychs.remove_11rb$(pos);
  }
  function Canvas(width, height) {
    this.canvas_0 = null;
    this.onclick = Canvas$onclick$lambda;
    var tmp$, tmp$_0;
    logWithTime(console, ['Initializing canvas...']);
    this.canvas_0 = Kotlin.isType(tmp$ = document.createElement('canvas'), HTMLCanvasElement) ? tmp$ : throwCCE();
    var context = Kotlin.isType(tmp$_0 = this.canvas_0.getContext('2d'), CanvasRenderingContext2D) ? tmp$_0 : throwCCE();
    context.canvas.width = width;
    context.canvas.height = height;
    ensureNotNull(document.body).appendChild(this.canvas_0);
    this.canvas_0.onclick = this.onclick;
  }
  Object.defineProperty(Canvas.prototype, 'cxt', {
    get: function () {
      var tmp$;
      return Kotlin.isType(tmp$ = this.canvas_0.getContext('2d'), CanvasRenderingContext2D) ? tmp$ : throwCCE();
    }
  });
  Object.defineProperty(Canvas.prototype, 'width', {
    get: function () {
      return this.canvas_0.width;
    }
  });
  Object.defineProperty(Canvas.prototype, 'height', {
    get: function () {
      return this.canvas_0.height;
    }
  });
  function Canvas$onclick$lambda(event) {
    var pos = event;
    logWithTime(console, ['Canvas click registered at (' + pos.x + ', ' + pos.y + ').']);
    var params = [pos.x, pos.y];
    var tych = json([new Pair('tych', params)]);
    gameSocket.send(JSON.stringify(tych));
    return Unit;
  }
  Canvas.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Canvas',
    interfaces: []
  };
  var cxt;
  function renderBackground($receiver) {
    $receiver.cxt.save();
    $receiver.cxt.fillStyle = '#FAFAFA';
    $receiver.cxt.fillRect(0.0, 0.0, canvas.width, canvas.height);
    $receiver.cxt.restore();
  }
  function renderTychs($receiver) {
    var tmp$;
    tmp$ = tychs.values.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      $receiver.cxt.save();
      $receiver.cxt.beginPath();
      $receiver.cxt.arc(element.position.x, element.position.y, element.currentRadius, 0.0, 2.0 * Math.PI);
      $receiver.cxt.fillStyle = 'rgba(242,160,111, 0.4)';
      $receiver.cxt.fill();
      $receiver.cxt.stroke();
      $receiver.cxt.restore();
    }
  }
  var LinkedHashSet_init = Kotlin.kotlin.collections.LinkedHashSet_init_287e2$;
  function shrinkTychs($receiver) {
    var deadTychs = LinkedHashSet_init();
    var tmp$;
    tmp$ = tychs.values.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      element.currentRadius = element.currentRadius - element.shrinkSpeedPerSecond / (1000.0 / RENDERING_RATIO);
      if (element.currentRadius <= 0) {
        deadTychs.add_11rb$(element);
      }
    }
    if (!deadTychs.isEmpty()) {
      logWithTime(console, ['Removing dead tychs...']);
      var tmp$_0;
      tmp$_0 = deadTychs.iterator();
      while (tmp$_0.hasNext()) {
        var element_0 = tmp$_0.next();
        tychs.remove_11rb$(element_0.position);
      }
    }
  }
  function renderScoreboard($receiver) {
    var y = {v: 15.0};
    $receiver.cxt.font = '12px Arial';
    $receiver.cxt.fillText('Scoreboard:', 10.0, y.v);
    var tmp$;
    tmp$ = scoreboard.entries.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      y.v += 15.0;
      var placement = y.v / 15 - 1;
      $receiver.cxt.fillText(placement.toString() + '. ' + element.key + ': ' + element.value, 10.0, y.v);
    }
    $receiver.cxt.restore();
  }
  function Position(x, y) {
    this.x = x;
    this.y = y;
  }
  Position.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Position',
    interfaces: []
  };
  Position.prototype.component1 = function () {
    return this.x;
  };
  Position.prototype.component2 = function () {
    return this.y;
  };
  Position.prototype.copy_lu1900$ = function (x, y) {
    return new Position(x === void 0 ? this.x : x, y === void 0 ? this.y : y);
  };
  Position.prototype.toString = function () {
    return 'Position(x=' + Kotlin.toString(this.x) + (', y=' + Kotlin.toString(this.y)) + ')';
  };
  Position.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.x) | 0;
    result = result * 31 + Kotlin.hashCode(this.y) | 0;
    return result;
  };
  Position.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.x, other.x) && Kotlin.equals(this.y, other.y)))));
  };
  function Tych(position, currentRadius, shrinkSpeedPerSecond) {
    this.position = position;
    this.currentRadius = currentRadius;
    this.shrinkSpeedPerSecond = shrinkSpeedPerSecond;
  }
  Tych.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Tych',
    interfaces: []
  };
  Tych.prototype.component1 = function () {
    return this.position;
  };
  Tych.prototype.component2 = function () {
    return this.currentRadius;
  };
  Tych.prototype.component3 = function () {
    return this.shrinkSpeedPerSecond;
  };
  Tych.prototype.copy_1kfbhp$ = function (position, currentRadius, shrinkSpeedPerSecond) {
    return new Tych(position === void 0 ? this.position : position, currentRadius === void 0 ? this.currentRadius : currentRadius, shrinkSpeedPerSecond === void 0 ? this.shrinkSpeedPerSecond : shrinkSpeedPerSecond);
  };
  Tych.prototype.toString = function () {
    return 'Tych(position=' + Kotlin.toString(this.position) + (', currentRadius=' + Kotlin.toString(this.currentRadius)) + (', shrinkSpeedPerSecond=' + Kotlin.toString(this.shrinkSpeedPerSecond)) + ')';
  };
  Tych.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.position) | 0;
    result = result * 31 + Kotlin.hashCode(this.currentRadius) | 0;
    result = result * 31 + Kotlin.hashCode(this.shrinkSpeedPerSecond) | 0;
    return result;
  };
  Tych.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.position, other.position) && Kotlin.equals(this.currentRadius, other.currentRadius) && Kotlin.equals(this.shrinkSpeedPerSecond, other.shrinkSpeedPerSecond)))));
  };
  var gameSocket;
  function initWebSockets$lambda(event) {
    logWithTime(console, ['Open game socket connection.']);
    var username = window.prompt('Choose username:');
    var login = json([to('username', username)]);
    var greetings = json([to('login', login)]);
    gameSocket.send(JSON.stringify(greetings));
    return event;
  }
  function initWebSockets$lambda_0(event) {
    var message = JSON.parse(event.data);
    var tych = message.tych;
    if (tych != null) {
      handleTych(tych);
    }
    var sb = message.scoreboard;
    if (sb != null) {
      handleScoreboard(sb);
    }
    var dtych = message.removeTych;
    if (dtych != null) {
      handleRemoveTych(dtych);
    }
    return event;
  }
  function initWebSockets$lambda_1(event) {
    logWithTime(console, ['Close game socket connection.']);
    return Unit;
  }
  function initWebSockets() {
    logWithTime(console, ['Init web sockets...']);
    gameSocket.onopen = initWebSockets$lambda;
    gameSocket.onmessage = initWebSockets$lambda_0;
    gameSocket.onclose = initWebSockets$lambda_1;
  }
  function logWithTime($receiver, o) {
    console.log((new Date()).getTime().toString() + ' > ' + o);
  }
  Object.defineProperty(_, 'tychs', {
    get: function () {
      return tychs;
    }
  });
  Object.defineProperty(_, 'scoreboard', {
    get: function () {
      return scoreboard;
    }
  });
  Object.defineProperty(_, 'canvas', {
    get: function () {
      return canvas;
    }
  });
  Object.defineProperty(_, 'RENDERING_RATIO', {
    get: function () {
      return RENDERING_RATIO;
    }
  });
  Object.defineProperty(_, 'UPDATE_RATIO', {
    get: function () {
      return UPDATE_RATIO;
    }
  });
  Object.defineProperty(_, 'SCOREBOARD_LIMIT', {
    get: function () {
      return SCOREBOARD_LIMIT;
    }
  });
  _.main_kand9s$ = main;
  _.updateScoreboard = updateScoreboard;
  _.handleTych_za3rmp$ = handleTych;
  _.handleScoreboard_za3rmp$ = handleScoreboard;
  _.handleRemoveTych_za3rmp$ = handleRemoveTych;
  _.Canvas = Canvas;
  Object.defineProperty(_, 'cxt', {
    get: function () {
      return cxt;
    }
  });
  _.renderBackground_ye3o09$ = renderBackground;
  _.renderTychs_ye3o09$ = renderTychs;
  _.shrinkTychs_ye3o09$ = shrinkTychs;
  _.renderScoreboard_ye3o09$ = renderScoreboard;
  _.Position = Position;
  _.Tych = Tych;
  Object.defineProperty(_, 'gameSocket', {
    get: function () {
      return gameSocket;
    }
  });
  _.initWebSockets = initWebSockets;
  _.logWithTime_taunjn$ = logWithTime;
  var LinkedHashMap_init = Kotlin.kotlin.collections.LinkedHashMap_init_q3lmfv$;
  tychs = LinkedHashMap_init();
  scoreboard = LinkedHashMap_init();
  canvas = new Canvas(window.innerWidth, window.innerHeight);
  RENDERING_RATIO = 10.0;
  UPDATE_RATIO = 1000.0;
  SCOREBOARD_LIMIT = 10.0;
  cxt = canvas.cxt;
  gameSocket = new WebSocket('ws://' + window.location.hostname + ':4567/game');
  main([]);
  Kotlin.defineModule('output', _);
  return _;
}(typeof output === 'undefined' ? {} : output, kotlin);
