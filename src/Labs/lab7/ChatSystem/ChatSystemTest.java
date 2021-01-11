package Labs.lab7.ChatSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

class ChatRoom {

    private String roomName;
    private Set<String> users;

    public ChatRoom(String roomName) {
        this.roomName = roomName;
        users = new TreeSet<>();
    }


    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(roomName + "\n");
        if (users.isEmpty())
            sb.append("EMPTY");
        else
            sb.append(String.join("\n", users));
        sb.append("\n");
        return sb.toString();
    }


    public boolean hasUser(String username) {
        return users.contains(username);
    }

    public int numUsers() {
        return users.size();
    }

    public String getRoomName() {
        return roomName;
    }

    public Set<String> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatRoom)) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return roomName.equals(chatRoom.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomName);
    }
}

class ChatSystem {

    private Map<String, ChatRoom> roomsByName;
    private Set<String> allUsers;
    private TreeSet<ChatRoom> allRooms;

    public ChatSystem() {
        roomsByName = new TreeMap<>();
        allUsers = new TreeSet<>();
        allRooms = new TreeSet<>(Comparator.comparing(ChatRoom::numUsers).thenComparing(ChatRoom::getRoomName));
    }

    public void addRoom(String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName);
        allRooms.add(chatRoom);
        roomsByName.putIfAbsent(roomName, chatRoom);
    }

    public void removeRoom(String roomName) {
        allRooms.remove(new ChatRoom(roomName));
        roomsByName.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if(!roomsByName.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        return roomsByName.get(roomName);
    }

    public void register(String username) {
        allUsers.add(username);

        if(!allRooms.isEmpty()) {
            allRooms.last().addUser(username);
        }
    }

    public void registerAndJoin(String username, String roomName) {
        allUsers.add(username);
        roomsByName.get(roomName).addUser(username);
    }

    public void joinRooms(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!roomsByName.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if (!allUsers.contains(username))
            throw new NoSuchUserException(username);
        roomsByName.get(roomName).addUser(username);
    }



}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }

}

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String roomName) {
        super("No such room " + roomName);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String userName) {
        super("No such user " + userName);
    }
}
