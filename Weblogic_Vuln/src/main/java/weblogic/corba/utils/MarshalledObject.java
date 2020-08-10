package weblogic.corba.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.Remote;
import java.util.Arrays;

public final class MarshalledObject implements Serializable {
    private byte[] objBytes = null;
    private int hash;

    public MarshalledObject(Object var1) throws IOException {
        if (var1 == null) {
            this.hash = 13;
        } else {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            MarshalledObject.MarshalledObjectOutputStream var3 = new MarshalledObject.MarshalledObjectOutputStream(var2);
            var3.writeObject(var1);
            var3.flush();
            this.objBytes = var2.toByteArray();
            int var4 = 0;

            for(int var5 = 0; var5 < this.objBytes.length; ++var5) {
                var4 = 31 * var4 + this.objBytes[var5];
            }

            this.hash = var4;
        }
    }

    public Object readResolve() throws IOException, ClassNotFoundException, ObjectStreamException {
        if (this.objBytes == null) {
            return null;
        } else {
            ByteArrayInputStream var1 = new ByteArrayInputStream(this.objBytes);
            ObjectInputStream var2 = new ObjectInputStream(var1);
            Object var3 = var2.readObject();
            var2.close();
            return var3;
        }
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean equals(Object var1) {
        if (var1 == this) {
            return true;
        } else {
            if (var1 != null && var1 instanceof MarshalledObject) {
                MarshalledObject var2 = (MarshalledObject)var1;
                if (this.objBytes == null || var2.objBytes == null) {
                    return this.objBytes == var2.objBytes;
                }

                if (Arrays.equals(this.objBytes, var2.objBytes)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static class MarshalledObjectOutputStream extends ObjectOutputStream {
        MarshalledObjectOutputStream(OutputStream var1) throws IOException {
            super(var1);
            this.enableReplaceObject(true);
        }

        public Object replaceObject(Object var1) throws IOException {
            if (var1 instanceof Remote) {
                throw new NotSerializableException("marshal remote object - " + var1.getClass().getName());
            } else {
                return var1;
            }
        }
    }
}
