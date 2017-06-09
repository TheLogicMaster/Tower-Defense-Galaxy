
package com.logicmaster63.tdgalaxy.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class ChatServer {
	Server server;

	public ChatServer () throws IOException {
		server = new Server() {
			protected Connection newConnection () {
				return new ChatConnection();
			}
		};

		com.logicmaster63.tdgalaxy.server.Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				ChatConnection connection = (ChatConnection)c;

				if (object instanceof com.logicmaster63.tdgalaxy.server.Network.RegisterName) {
					if (connection.name != null) return;
					String name = ((com.logicmaster63.tdgalaxy.server.Network.RegisterName)object).name;
					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					connection.name = name;
					com.logicmaster63.tdgalaxy.server.Network.ChatMessage chatMessage = new com.logicmaster63.tdgalaxy.server.Network.ChatMessage();
					chatMessage.text = name + " connected.";
					server.sendToAllExceptTCP(connection.getID(), chatMessage);
					updateNames();
					return;
				}

				if (object instanceof com.logicmaster63.tdgalaxy.server.Network.ChatMessage) {
					if (connection.name == null) return;
					com.logicmaster63.tdgalaxy.server.Network.ChatMessage chatMessage = (com.logicmaster63.tdgalaxy.server.Network.ChatMessage)object;
					String message = chatMessage.text;
					if (message == null) return;
					message = message.trim();
					if (message.length() == 0) return;
					chatMessage.text = connection.name + ": " + message;
					server.sendToAllTCP(chatMessage);
					return;
				}
			}

			public void disconnected (Connection c) {
				ChatConnection connection = (ChatConnection)c;
				if (connection.name != null) {
					com.logicmaster63.tdgalaxy.server.Network.ChatMessage chatMessage = new com.logicmaster63.tdgalaxy.server.Network.ChatMessage();
					chatMessage.text = connection.name + " disconnected.";
					server.sendToAllTCP(chatMessage);
					updateNames();
				}
			}
		});
		server.bind(com.logicmaster63.tdgalaxy.server.Network.port);
		server.start();

		JFrame frame = new JFrame("Chat Server");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed (WindowEvent evt) {
				server.stop();
			}
		});
		frame.getContentPane().add(new JLabel("Close to stop the chat server."));
		frame.setSize(320, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	void updateNames () {
		Connection[] connections = server.getConnections();
		ArrayList names = new ArrayList(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			ChatConnection connection = (ChatConnection)connections[i];
			names.add(connection.name);
		}
		com.logicmaster63.tdgalaxy.server.Network.UpdateNames updateNames = new com.logicmaster63.tdgalaxy.server.Network.UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		server.sendToAllTCP(updateNames);
	}

	static class ChatConnection extends Connection {
		public String name;
	}

	public static void main (String[] args) throws IOException {
		Log.set(Log.LEVEL_DEBUG);
		new ChatServer();
	}
}
