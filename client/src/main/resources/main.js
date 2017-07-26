if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'main'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'main'.");
}
var main = function (_, Kotlin) {
  'use strict';
  var Pair = Kotlin.kotlin.Pair;
  var json = Kotlin.kotlin.js.json_pyyo18$;
  var lazy = Kotlin.kotlin.lazy_klfg04$;
  function canvas$lambda$lambda(event) {
    var params = [event.x, event.y];
    var tych = json([new Pair('tych', params)]);
    gameSocket.send(JSON.stringify(tych));
  }
  function canvas$lambda() {
    var tmp$, tmp$_0, tmp$_1;
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
  function drawTych(pos) {
    get_cxt().save();
    get_cxt().shadowColor = '#000000';
    get_cxt().shadowBlur = 5.0;
    get_cxt().shadowOffsetX = -4.0;
    get_cxt().shadowOffsetY = 4.0;
    get_cxt().fillStyle = 'rgb(242,160,110)';
    get_cxt().fillText('WOW', pos.x, pos.y);
    get_cxt().restore();
  }
  function renderBackground() {
    get_cxt().save();
    get_cxt().fillStyle = '#FAFAFA';
    get_cxt().fillRect(0.0, 0.0, get_width(), get_height());
    get_cxt().restore();
  }
  function main$lambda() {
    renderBackground();
  }
  function main(args) {
    initWebSockets();
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
  function Tych(args) {
    this.args = args;
  }
  Tych.$metadata$ = {
    kind: Kotlin.Kind.CLASS,
    simpleName: 'Tych',
    interfaces: []
  };
  Tych.prototype.component1 = function () {
    return this.args;
  };
  Tych.prototype.copy_awc180$ = function (args) {
    return new Tych(args === void 0 ? this.args : args);
  };
  Tych.prototype.toString = function () {
    return 'Tych(args=' + Kotlin.toString(this.args) + ')';
  };
  Tych.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.args) | 0;
    return result;
  };
  Tych.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && Kotlin.equals(this.args, other.args))));
  };
  var gameSocket;
  function initWebSockets$lambda(event) {
    var username = window.prompt('Choose username:');
    var login = json([new Pair('username', username)]);
    var greetings = json([new Pair('login', login)]);
    gameSocket.send(JSON.stringify(greetings));
    return event;
  }
  function initWebSockets$lambda_0(event) {
    var message = JSON.parse(event.data);
    var tych = message.tych;
    var pos = new Position(tych[0], tych[1]);
    drawTych(pos);
    return event;
  }
  function initWebSockets() {
    gameSocket.onopen = initWebSockets$lambda;
    gameSocket.onmessage = initWebSockets$lambda_0;
  }
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
  _.drawTych_dfplqh$ = drawTych;
  _.renderBackground = renderBackground;
  _.main_kand9s$ = main;
  _.Position = Position;
  _.Tych = Tych;
  Object.defineProperty(_, 'gameSocket', {
    get: function () {
      return gameSocket;
    }
  });
  _.initWebSockets = initWebSockets;
  canvas = lazy(canvas$lambda);
  gameSocket = new WebSocket('ws://localhost:4567/game');
  main([]);
  Kotlin.defineModule('main', _);
  return _;
}(typeof main === 'undefined' ? {} : main, kotlin);
