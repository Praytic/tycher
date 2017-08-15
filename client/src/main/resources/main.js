if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'main'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'main'.");
}
var main = function (_, Kotlin) {
  'use strict';
  var Pair = Kotlin.kotlin.Pair;
  var json = Kotlin.kotlin.js.json_pyyo18$;
  var lazy = Kotlin.kotlin.lazy_klfg04$;
  var tychs;
  function logWithTime($receiver, o) {
    console.log((new Date()).getTime().toString() + ' > ' + o);
  }
  function canvas$lambda$lambda(event) {
    var pos = event;
    logWithTime(console, ['Canvas click registered at (' + pos.x + ', ' + pos.y + ').']);
    var params = [pos.x, pos.y];
    var tych = json([new Pair('tych', params)]);
    gameSocket.send(JSON.stringify(tych));
  }
  function canvas$lambda() {
    var tmp$, tmp$_0, tmp$_1;
    logWithTime(console, ['Initializing canvas...']);
    var canvas = Kotlin.isType(tmp$ = document.createElement('canvas'), HTMLCanvasElement) ? tmp$ : Kotlin.throwCCE();
    var context = Kotlin.isType(tmp$_0 = canvas.getContext('2d'), CanvasRenderingContext2D) ? tmp$_0 : Kotlin.throwCCE();
    context.canvas.width = window.innerWidth;
    context.canvas.height = window.innerHeight;
    ((tmp$_1 = document.body) != null ? tmp$_1 : Kotlin.throwNPE()).appendChild(canvas);
    canvas.onclick = canvas$lambda$lambda;
    return canvas;
  }
  var canvas;
  function get_canvas() {
    new Kotlin.PropertyMetadata('canvas');
    return canvas.value;
  }
  function get_cxt() {
    var tmp$;
    return Kotlin.isType(tmp$ = get_canvas().getContext('2d'), CanvasRenderingContext2D) ? tmp$ : Kotlin.throwCCE();
  }
  function get_width() {
    return get_canvas().width;
  }
  function get_height() {
    return get_canvas().height;
  }
  function renderBackground() {
    get_cxt().save();
    get_cxt().fillStyle = '#FAFAFA';
    get_cxt().fillRect(0.0, 0.0, get_width(), get_height());
    get_cxt().restore();
  }
  function renderTychs() {
    var tmp$;
    tmp$ = tychs.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      get_cxt().save();
      get_cxt().beginPath();
      get_cxt().arc(element.position.x, element.position.y, element.currentRadius, 0.0, 2.0 * Math.PI);
      get_cxt().fillStyle = 'rgb(242,160,111)';
      get_cxt().fill();
      get_cxt().stroke();
      get_cxt().restore();
    }
  }
  function shrinkTychs() {
    var tmp$;
    tmp$ = tychs.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      element.currentRadius = element.currentRadius - element.shrinkSpeed;
    }
    var $receiver = tychs;
    var destination = Kotlin.kotlin.collections.ArrayList_init_ww73n8$();
    var tmp$_0;
    tmp$_0 = $receiver.iterator();
    while (tmp$_0.hasNext()) {
      var element_0 = tmp$_0.next();
      if (element_0.currentRadius <= 0) {
        destination.add_11rb$(element_0);
      }
    }
  }
  function main$lambda() {
    renderBackground();
    renderTychs();
  }
  function main(args) {
    initWebSockets();
    logWithTime(console, ['Starting game loop...']);
    window.setInterval(main$lambda, 50);
  }
  function Position(x, y) {
    this.x = x;
    this.y = y;
  }
  Position.$metadata$ = {
    kind: Kotlin.Kind.CLASS,
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
  function Tych(position, currentRadius, shrinkSpeed) {
    this.position = position;
    this.currentRadius = currentRadius;
    this.shrinkSpeed = shrinkSpeed;
  }
  Tych.$metadata$ = {
    kind: Kotlin.Kind.CLASS,
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
    return this.shrinkSpeed;
  };
  Tych.prototype.copy_1kfbhp$ = function (position, currentRadius, shrinkSpeed) {
    return new Tych(position === void 0 ? this.position : position, currentRadius === void 0 ? this.currentRadius : currentRadius, shrinkSpeed === void 0 ? this.shrinkSpeed : shrinkSpeed);
  };
  Tych.prototype.toString = function () {
    return 'Tych(position=' + Kotlin.toString(this.position) + (', currentRadius=' + Kotlin.toString(this.currentRadius)) + (', shrinkSpeed=' + Kotlin.toString(this.shrinkSpeed)) + ')';
  };
  Tych.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.position) | 0;
    result = result * 31 + Kotlin.hashCode(this.currentRadius) | 0;
    result = result * 31 + Kotlin.hashCode(this.shrinkSpeed) | 0;
    return result;
  };
  Tych.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.position, other.position) && Kotlin.equals(this.currentRadius, other.currentRadius) && Kotlin.equals(this.shrinkSpeed, other.shrinkSpeed)))));
  };
  var gameSocket;
  function initWebSockets$lambda(event) {
    logWithTime(console, ['Open game socket connection.']);
    var username = window.prompt('Choose username:');
    var login = json([new Pair('username', username)]);
    var greetings = json([new Pair('login', login)]);
    gameSocket.send(JSON.stringify(greetings));
    return event;
  }
  function initWebSockets$lambda_0(event) {
    var message = JSON.parse(event.data);
    var tych = message.tych;
    logWithTime(console, ['Message game socket with data: (' + tych + ')']);
    var pos = new Position(tych[0], tych[1]);
    var radius = tych[2];
    var shrinkSpeed = tych[3];
    tychs.add_11rb$(new Tych(pos, radius, shrinkSpeed));
    return event;
  }
  function initWebSockets() {
    logWithTime(console, ['Init web sockets...']);
    gameSocket.onopen = initWebSockets$lambda;
    gameSocket.onmessage = initWebSockets$lambda_0;
  }
  Object.defineProperty(_, 'tychs', {
    get: function () {
      return tychs;
    }
  });
  _.logWithTime_taunjn$ = logWithTime;
  Object.defineProperty(_, 'canvas', {
    get: get_canvas
  });
  Object.defineProperty(_, 'cxt', {
    get: get_cxt
  });
  Object.defineProperty(_, 'width', {
    get: get_width
  });
  Object.defineProperty(_, 'height', {
    get: get_height
  });
  _.renderBackground = renderBackground;
  _.renderTychs = renderTychs;
  _.shrinkTychs = shrinkTychs;
  _.main_kand9s$ = main;
  _.Position = Position;
  _.Tych = Tych;
  Object.defineProperty(_, 'gameSocket', {
    get: function () {
      return gameSocket;
    }
  });
  _.initWebSockets = initWebSockets;
  tychs = Kotlin.kotlin.collections.LinkedHashSet_init_287e2$();
  canvas = lazy(canvas$lambda);
  gameSocket = new WebSocket('ws://localhost:4567/game');
  main([]);
  Kotlin.defineModule('main', _);
  return _;
}(typeof main === 'undefined' ? {} : main, kotlin);
