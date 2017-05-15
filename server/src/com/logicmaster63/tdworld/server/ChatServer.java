
package com.logicmaster63.tdworld.server;

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

		Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				ChatConnection connection = (ChatConnection)c;

				if (object instanceof Network.RegisterName) {
					if (connection.name != null) return;
					String name = ((Network.RegisterName)object).name;
					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					connection.name = name;
					Network.ChatMessage chatMessage = new Network.ChatMessage();
					chatMessage.text = name + " connected.";
					server.sendToAllExceptTCP(connection.getID(), chatMessage);
					updateNames();
					return;
				}

				if (object instanceof Network.ChatMessage) {
					if (connection.name == null) return;
					Network.ChatMessage chatMessage = (Network.ChatMessage)object;
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
					Network.ChatMessage chatMessage = new Network.ChatMessage();
					chatMessage.text = connection.name + " disconnected.";
					server.sendToAllTCP(chatMessage);
					updateNames();
				}
			}
		});
		server.bind(Network.port);
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
		Network.UpdateNames updateNames = new Network.UpdateNames();
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
