package Labs.lab7.ChatSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

class ChatRoom implements Comparable<ChatRoom>{ /////////////////dqwdqwasdasda nemam idea..................

    private String roomName;
    private TreeSet<String> users;
    public static Comparator<ChatRoom> comparator = Comparator.comparingInt(ChatRoom::numUsers)
            .thenComparing(ChatRoom::getRoomName).reversed();   // nemam idea zaso ne rabote !!!!!!!!!!!!!!!!!!!

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
    public int compareTo(ChatRoom chatRoom) {
        return comparator.compare(this, chatRoom);
    }
}

class ChatSystem {

    private Set<String> allUsers;
    private TreeSet<ChatRoom> allRooms;
    private Map<String, ChatRoom> roomsByName;


    public ChatSystem() {
        roomsByName = new TreeMap<>();
        allUsers = new TreeSet<>();
        allRooms = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName);
        allRooms.add(chatRoom);
        roomsByName.putIfAbsent(roomName, chatRoom);

    }

    public void removeRoom(String roomName) {
        ChatRoom chatRoom = roomsByName.get(roomName);
        roomsByName.remove(roomName);
        allRooms.remove(chatRoom);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if(!roomsByName.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        return roomsByName.get(roomName);
    }

    public void register(String username) {
        allUsers.add(username);

        if(!allRooms.isEmpty()) {
            ChatRoom room = allRooms.last();
            allRooms.remove(room);
            room.addUser(username);
            allRooms.add(room);             // promenata na ovoj ChatRoom ke se reflektira i vo TreeMap roomsByName
        }

    }

    public void registerAndJoin(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        allUsers.add(username);
        joinRooms(username, roomName);
    }

    public void joinRooms(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!roomsByName.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if (!allUsers.contains(username))
            throw new NoSuchUserException(username);
        ChatRoom room = roomsByName.get(roomName);    // ista prikazna
        allRooms.remove(room);
        room.addUser(username);
        allRooms.add(room);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!roomsByName.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if (!allUsers.contains(username))
            throw new NoSuchUserException(username);

        ChatRoom room = roomsByName.get(roomName);
        allRooms.remove(room);
        room.removeUser(username);
        allRooms.add(room);
    }

    public void followFriend(String userName, String friend_username) throws NoSuchUserException {
        if (!allUsers.contains(friend_username))
            throw new NoSuchUserException(friend_username);
        List<ChatRoom> friendRooms = roomsByName.values()
                .stream()
                .filter(room -> room.hasUser(friend_username))
                .collect(Collectors.toList());

        for(ChatRoom friendRoom : friendRooms) {
            allRooms.remove(friendRoom);
            friendRoom.addUser(userName);
            allRooms.add(friendRoom);
        }
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
