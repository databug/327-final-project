package com.fp;

import java.net.*;

public class Client extends BaseServer
{
    Client( int port, String addr )
    {
        super( port, addr );
    }

    public void run()
    {
        System.out.println("Running client code");
        try {

            // Starts the EventQeuue thread.
            startEQ( 0 );

            // Sets up the socket for sending  datagram packets.
            setupSender();

            // Register with the server.
            send( "reg:" + Integer.toString(eq.getPort()), addr, port );

            while( true )
            {
                // Handles the events
                handleEvents();
            }

        } catch( Exception e ) {
            e.printStackTrace();
        } finally {

            if( socket != null )
                socket.close();
        }
    }

    public void handleEvents()
    {
        while( eq.isEmpty() == false )
        {
            DatagramPacket dp  = eq.poll();
            String message = EventQueue.getString( dp );
            String mes[] = message.split(":");

            switch( mes[0] )
            {
                case "reg":
                    register( dp.getAddress(), dp.getPort(), Integer.parseInt(mes[1].trim()) );
                    break;

                default:
                    System.out.printf("Received Message: %s\n", message);
                    break;
            }
        }
    }

    private DatagramSocket socket;
    InetAddress serverAddress;
}
