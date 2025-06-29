type MessageHandler = (payload: any) => void;

class WebSocketClient {
  private socket: WebSocket | null = null;
  private readonly url: string;
  private handlers: Map<string, MessageHandler> = new Map();
  private manuallyClosed = false;
  private reconnectDelay = 3000;

  private connectedPromise!: Promise<void>;
  private connectedResolve!: () => void;

  constructor(url: string) {
    this.url = url;
  }

  isWebSocketConnected = (): boolean => {
    return this.socket?.readyState === WebSocket.OPEN;
  };

  connect() {
    if (this.isWebSocketConnected()) return;

    this.connectedPromise = new Promise((resolve) => {
      this.connectedResolve = resolve;
    });

    this.socket = new WebSocket(this.url);

    this.socket.onopen = () => {
      this.send({ type: 'getRequestId' });
    };

    this.socket.onmessage = (event: MessageEvent) => {
      try {
        const msg = JSON.parse(event.data);
        const { type, payload, requestId } = msg;
        if (type === 'bindAck' && requestId) {
          localStorage.setItem('requestId', requestId);
          console.log('🔗 绑定成功，requestId:', requestId);
          this.connectedResolve();
        } else if (this.handlers.has(type)) {
          this.handlers.get(type)?.(payload);
        } else {
          console.warn(`📭 未处理的消息类型: ${type}`, payload);
        }
      } catch (e) {
        console.error('⚠️ JSON 解析失败:', event.data);
      }
    };

    this.socket.onerror = (e) => {
      console.error('WebSocket error:', e);
    };

    this.socket.onclose = () => {
      console.warn('WebSocket 断开连接');
      if (!this.manuallyClosed) {
        setTimeout(() => this.connect(), this.reconnectDelay);
      }
    };
  }

  async waitUntilConnected(): Promise<void> {
    if (this.isWebSocketConnected()) {
      return Promise.resolve();
    }
    return this.connectedPromise;
  }

  send(data: any) {
    if (this.isWebSocketConnected()) {
      this.socket!.send(JSON.stringify(data));
    } else {
      console.warn('WebSocket 尚未连接，消息未发送');
    }
  }

  on(type: string, handler: MessageHandler) {
    this.handlers.set(type, handler);
  }

  close() {
    this.manuallyClosed = true;
    this.socket?.close();
  }
}

export const wsClient = new WebSocketClient('ws://localhost:8081/ws');
