package org.jtester.module.jmockit;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import mockit.Cascading;

import org.jtester.junit.JTester;
import org.junit.Ignore;
import org.junit.Test;

public class CascadeMockDemoTest implements JTester {
    @Test
    @Ignore
    public void recordAndVerifyExpectationsOnCascadedMocks(@Cascading final Socket mock) throws Exception {
        new NonStrictExpectations() {
            {
                mock.getChannel().isConnected();
                result = true;
                mock.getChannel().connect((SocketAddress) any);
                returns(true);
            }
        };

        // Inside production code:
        Socket s = new Socket("remoteHost", 6555);
        want.bool(s.getChannel().isConnected()).isEqualTo(true);

        SocketAddress sa = new InetSocketAddress("remoteHost", 6555);
        boolean bl = s.getChannel().connect(sa);
        want.bool(bl).isEqualTo(true);
    }
}
