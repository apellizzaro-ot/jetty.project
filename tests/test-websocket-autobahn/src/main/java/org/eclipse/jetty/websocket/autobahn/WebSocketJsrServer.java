//
//  ========================================================================
//  Copyright (c) 1995-2019 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.websocket.autobahn;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.JavaxWebSocketServerContainerInitializer;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;

/**
 * Example of setting up a javax.websocket server with Jetty embedded
 */
public class WebSocketJsrServer
{
    /**
     * A server socket endpoint
     */
    @ServerEndpoint( value = "/" )
    public static class EchoJsrSocket
    {
        @OnMessage
        public void onMessage( Session session, String message )
        {
            session.getAsyncRemote().sendText( message );
        }

        @OnOpen
        public void onOpen( Session session, EndpointConfig endpointConfig ){
            session.setMaxTextMessageBufferSize( Integer.MAX_VALUE );
            session.setMaxBinaryMessageBufferSize( Integer.MAX_VALUE );
        }

    }

    public static void main( String[] args )
        throws Exception
    {
        Server server = new Server( Integer.parseInt( args[0] ) );

        ServletContextHandler context = new ServletContextHandler( ServletContextHandler.SESSIONS );
        context.setContextPath( "/" );
        server.setHandler( context );

        ServerContainer wsContainer = JavaxWebSocketServerContainerInitializer.configureContext( context );

        wsContainer.addEndpoint( EchoJsrSocket.class );

        server.start();
        context.dumpStdErr();
        server.join();
    }
}