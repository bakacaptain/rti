package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class ByteSerialzier {

	public static byte[] serialize(Object obj) throws IOException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bout);
		oout.writeObject(obj);
		return bout.toByteArray();
	}
	
	public static Object deserialize(byte[] bytes) throws ClassNotFoundException, IOException{
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		ObjectInputStream oin = new ObjectInputStream(bin);
		return oin.readObject();
	}
}
