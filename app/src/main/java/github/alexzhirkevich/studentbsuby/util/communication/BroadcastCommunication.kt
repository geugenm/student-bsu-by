package github.alexzhirkevich.studentbsuby.util.communication

/**
 * Allows cross instance communication.
 * All [BroadcastCommunication]'s listen to all [BroadcastMapper]'s with the same [action].
 * @throws ClassCastException if communication and mapper has same action and different type-parameter [T]
 * */
interface BroadcastCommunication<T> : Communication<T>, Broadcast<T>
