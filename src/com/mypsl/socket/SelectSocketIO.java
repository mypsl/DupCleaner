import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class SelectSocketIO {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        Selector selector = Selector.open() ;
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器开始运行...");
        while(selector.select() > 0){
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    ByteBuffer outBuf = ByteBuffer.allocateDirect(1024);
                    String msg = "当前时间：" + new Date()+"-客户端："+client.socket().getLocalSocketAddress() + ":" +client.socket().getLocalPort();
                    outBuf.put(msg.getBytes());
                    outBuf.flip();
                    client.write(outBuf);
                    client.register(selector, SelectionKey.OP_READ, outBuf);
                }else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel)key.channel();
                    ByteBuffer inBuf = (ByteBuffer)key.attachment();
                    inBuf.clear();
                    int read = 0;
                    while (true) {
                        read = client.read(inBuf);
                        if (0 < read) {
                            inBuf.flip();
                            while (inBuf.hasRemaining()) {
                                client.write(inBuf);
                            }
                        } else if (0 == read) {
                            break;
                        } else {
                            client.close();
                            break;
                        }
                    }

                }
            }

            //do something with socketChannel...
        }
    }
}
