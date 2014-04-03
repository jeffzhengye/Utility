/*
 * LingPipe v. 3.8
 * Copyright (C) 2003-2009 Alias-i
 *
 * This program is licensed under the Alias-i Royalty Free License
 * Version 1 WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Alias-i
 * Royalty Free License Version 1 for more details.
 *
 * You should have received a copy of the Alias-i Royalty Free License
 * Version 1 along with this program; if not, visit
 * http://alias-i.com/lingpipe/licenses/lingpipe-license-1.txt or contact
 * Alias-i, Inc. at 181 North 11th Street, Suite 401, Brooklyn, NY 11211,
 * +1 (718) 290-9170.
 */

package org.dutir.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.log4j.Logger;


/**
 * The <code>AbstractExternalizer</code> is an adapter for read
 * resolved externalizables.  This provides a general mechanism to
 * write objects to an object output and read them back in.  Instances
 * of this class may be written and read from object streams in the
 * usual way (see {@link java.io.Serializable}, {@link Externalizable}, {@link
 * ObjectInput} and {@link ObjectOutput} for details.
 *
 * <P>Concrete subclasses of this class will define a {@link
 * #writeExternal(ObjectOutput)} method which writes an object, a {@link
 * #read(ObjectInput)} method which returns an object. Subclasses must
 * also implement a public nullary constructor.  <i>None</i> of the
 * methods in this class should be called directly.  The Java
 * interface specification requires the reads and writes to be public
 * and the resolver to be accessible to the subclass.
 *
 * <P>When an instance of this class is written to an object output by
 * serialization, the method {@link #writeExternal(ObjectOutput)} is
 * called.  When an instance is read back in by deserialization, first
 * an instance is created using the nullary constructor.  Then {@link
 * #readExternal(ObjectInput)} is called, which is implemented by this
 * class to call {@link #read(ObjectInput)} and store the result in a
 * member variable.  Finally, serialization calls {@link
 * #readResolve()} to get the object read.
 *
 * <P>This class is employed throughout LingPipe to carry out
 * compilation of classes for two reasons.  First, it allows the
 * compiled objects to have final variables set, which supports
 * LingPipe's extensive use of immutables.  Second, it avaoids the
 * messiness of exposing the I/O methods required for externalization
 * and deserialization, most notably the no-argument constructor.
 * This class is used as the superclass of a private
 * private internal class that does the actual compilation.  This
 * private internal class implements the required no-arg constructor
 * and stores the object required for {@link #readResolve()}.
 *
 * @author  Bob Carpenter
 * @version 4.0
 * @since   LingPipe2.0
 */
public abstract class AbstractExternalizable implements Externalizable {

    static final long serialVersionUID = -3880451086025138660L;
    static Logger logger = Logger.getLogger(AbstractExternalizable.class);
    private Object mObjectRead;

    /**
     * Construct an abstract externalizable.  Concrete subclasses must
     * provide a no-argument constructor or the compiled object will
     * throw a {@link java.io.InvalidClassException} when it attempts
     * to invoke the no-argument constructor through reflection.
     *
     * <P>Like the other methods, this method should <i>not</i> be
     * called directly.  See the class documentation above for
     * details.
     */
    protected AbstractExternalizable() {
        /* do nothing */
    }

    /**
     * Read an object from the specified input stream and return it.
     * Concrete subclasses implement this method in order to define
     * deserialization behavior -- the object returned is the one
     * produced by this object.
     *
     * <P>Like the other methods, this method should <i>not</i> be
     * called directly.  See the class documentation above for details.
     *
     * @param in Object input from which to read an object.
     * @throws IOException If there is an I/O exception reading.
     * @throws ClassNotFoundException If a class required for
     * deserialization is not loadable.
     */
    protected abstract Object read(ObjectInput in)
        throws ClassNotFoundException, IOException;

    /**
     * Writes an object to the specified object output.  Writes should
     * match reads in that the output produced by this method should
     * be readable using {@link #read(ObjectInput)}.
     *
     * <P>Like the other methods, this method should <i>not</i> be
     * called directly.  See the class documentation above for details.
     *
     * @param out Object output to which an object is written.
     * @throws IOException If there is an I/O exception writing.
     */
    public abstract void  writeExternal(ObjectOutput out)
        throws IOException;

    /**
     * Read an object from the specified input using and store it for
     * later resolution.
     *
     * <P>Like the other methods, this method should <i>not</i> be
     * called directly.  See the class documentation above for details.
     *
     * @param objIn Object input from which to read an object.
     * @throws IOException If there is an I/O exception reading.
     */
    public final void readExternal(ObjectInput objIn)
        throws ClassNotFoundException, IOException {

        mObjectRead = read(objIn);
    }

    /**
     * Returns the object read.
     *
     * <P>Like the other methods, this method should <i>not</i> be
     * called directly.  See the class documentation above for details.
     *
     * @return The last object read.
     */
    protected Object readResolve() {
        return mObjectRead;
    }

    /**
     * Compiles the specified compilable object to the specified file.
     *
     * @param compilable Object to compile.
     * @param file File to which object is written.
     * @throws IOException If there is an underlying I/O error.
     */
    public static void compileTo(Compilable compilable, File file)
        throws IOException {

        FileOutputStream fileOut = null;
        BufferedOutputStream bufOut = null;
        ObjectOutputStream objOut = null;
        try {
            fileOut = new FileOutputStream(file);
            bufOut = new BufferedOutputStream(fileOut);
            objOut = new ObjectOutputStream(bufOut);
            compilable.compileTo(objOut);
        } finally {
            Streams.closeOutputStream(objOut);
            Streams.closeOutputStream(bufOut);
            Streams.closeOutputStream(fileOut);
        }
    }

    /**
     * Serializes the specified serializable object to the specified
     * file.
     *
     *@Note:Zheng,  the file is locked before writing, so that other processes can not read or revise simultaneously 
     *
     * @param serializable Object to serialize.
     * @param file File to which the object is serialized.
     * @throws IOException If there is an underlying I/O error duruing
     * serialization.
     */
    public static void serializeTo(Serializable serializable, File file)
        throws IOException {

        FileOutputStream fileOut = null;
        BufferedOutputStream bufOut = null;
        ObjectOutputStream objOut = null;
        FileLock filelock = null;
        try {
            fileOut = new FileOutputStream(file);
            FileChannel channel = fileOut.getChannel();
            filelock = channel.tryLock();
            while(true){
            	if(filelock != null){
            		break;
            	}else{
            		if(logger.isInfoEnabled()) logger.info("cache file locked, can not be reserializeTo");
            		Thread.sleep(3000);
            		filelock = channel.tryLock();
            	}
            }
            bufOut = new BufferedOutputStream(fileOut);
            objOut = new ObjectOutputStream(bufOut);
            
            objOut.writeObject(serializable);
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            Streams.closeOutputStream(objOut);
            Streams.closeOutputStream(bufOut);
            Streams.closeOutputStream(fileOut);
		    if (filelock != null && filelock.isValid())
				filelock.release();
		    if(logger.isInfoEnabled()) logger.info("cache file locke released");
        }
    }

    /**
     * Serialize the object to the output if it is serializable, else
     * compile it to the output if it is compilable but not serializable.
     *
     * @param obj Object to serialize or compile.
     * @param out Output stream to which to write the object.
     * @throws IllegalArgumentException If the specified object is
     * neither serializable nor compilable.
     */
    public static void serializeOrCompile(Object obj, ObjectOutput out)
        throws IOException {

        if (obj instanceof Serializable) {
            out.writeObject(obj);
        } else if (obj instanceof Compilable) {
            ((Compilable) obj).compileTo(out);
        } else {
            String msg = "Object must be compilable or serializable."
                + " Found object with class=" + obj.getClass();
            throw new IllegalArgumentException(msg);
        }
    }


    /**
     * Compile the object to the output if it is compilable, else
     * serialize it to the output if it is serializable but not
     * compilable.
     *
     * @param obj Object to compile or serializable.
     * @param out Output stream to which to write the object.
     * @throws NotSerializableException If the specified object is
     * neither serializable nor compilable.
     * @throws IOException if there is an underlying I/O error.
     */
    public static void compileOrSerialize(Object obj, ObjectOutput out)
        throws IOException {

        if (obj instanceof Compilable) {
            ((Compilable) obj).compileTo(out);
        } else if (obj instanceof Serializable) {
            out.writeObject(obj);
        } else {
            String msg = "Object must be compilable or serializable."
                + " Found object with class=" + obj.getClass();
            throw new NotSerializableException(msg);
        }
    }


    /**
     * Serializes the specified object to the specified file.
     *
     * @param serializable Object to serialize.
     * @param file File to which object is written.
     * @throws IOException If there is an underlying I/O error.
    public static void serializeTo(Serializable serializable, File file)
        throws IOException {

        FileOutputStream fileOut = null;
        BufferedOutputStream bufOut = null;
        ObjectOutputStream objOut = null;
        try {
            fileOut = new FileOutputStream(file);
            bufOut = new BufferedOutputStream(fileOut);
            objOut = new ObjectOutputStream(bufOut);
            objOut.writeObject(serializable);
        } finally {
            Streams.closeOutputStream(objOut);
            Streams.closeOutputStream(bufOut);
            Streams.closeOutputStream(fileOut);
        }
    }
    */

    /**
     * Returns the result of reading a serialized object stored
     * in the specified file.
     *
     * <p><i>Implementation Note:</i> This is just a convenience
     * method that creates a file input stream, buffers it,
     * wraps it in an object input stream and reads an object from
     * the input.  It always makes sure to close all of the stream,
     * even if exceptions are raised.
     * @Note: the file is locked before read, so that other processes can not revise simultaneously 
     *
     * @param file File from which to read the object.
     * @return The object read from the file.
     * @throws IOException If there is an underlying I/O error while
     * reading.
     * @throws ClassNotFoundException If the classloader could not load
     * the class for the serialized object.
     */
    public static Object readObject(File file)
        throws IOException, ClassNotFoundException {

    	RandomAccessFile rfile = null;
    	
    	FileInputStream fis = null;
        BufferedInputStream bufIn = null;
        ObjectInputStream objIn = null;
        FileLock filelock = null;
        try {
        	rfile = new RandomAccessFile(file, "rw");
        	FileChannel channel = rfile.getChannel();
        	filelock = channel.tryLock();
            while(true){
            	if(filelock != null){
            		break;
            	}else{
            		Thread.sleep(1000);
            		if(logger.isInfoEnabled()) logger.info("cache file is locked, i am waiting");
            		filelock = channel.tryLock();
            	}
            }
        	if(logger.isInfoEnabled()) logger.info("read cache file");
        	fis = new FileInputStream(file);
            byte[] buf = new byte[1024*1024*3];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len =-1;
            while( (len = fis.read(buf)) != -1){
            	baos.write(buf, 0, len);
            }
            Streams.closeInputStream(fis);
            System.out.println("finish reading cache file");
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            
           
            bufIn = new BufferedInputStream(bais);
            objIn = new ObjectInputStream(bufIn);            
            return objIn.readObject();
        } catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
		    Streams.closeInputStream(objIn);
            Streams.closeInputStream(bufIn);
           
            rfile.close();
		    if (filelock != null && filelock.isValid())
				{
		    		filelock.release();
		    		
				}
        }
    }

    /**
     * Return the compiled form of the specified compilable.
     *
     * <p>See {@link #serializeDeserialize(Serializable)} for
     * a similar method that operates through the Serializable
     * interface rather than the compilable interface.
     *
     * <P><i>Implementation Note:</i> The model is written to a byte
     * array using <code>compileTo</code> and then read back in using
     * <code>ObjectInput</code>'s <code>readObject</code> method.
     * This means that both the object being compiled and the result
     * will typically be held in memory at one time.  After compiling
     * to a file, the object being compiled may be garbage collected
     * before the compiled object is read in from the file.
     *
     * @param c Object to compile.
     * @return Compiled form of object.
     * @throws ClassNotFoundException If the class of the compiled object
     * cannot be found.
     * @throws IOException If there is an I/O exception compiling or
     * deserializing the compilable, or if there is a class not found
     * exception thrown while deserializing.
     */
    public static Object compile(Compilable c)
        throws ClassNotFoundException, IOException {

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        c.compileTo(objOut);
        ByteArrayInputStream bytesIn
            = new ByteArrayInputStream(bytesOut.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);
        try {
            return objIn.readObject();
        } catch (ClassNotFoundException e) {
            String msg = "Compile i/o class not found exception=" + e;
            throw new IOException(msg);
        }
    }

    /**
     * Returns the result of serializing the specified object and then
     * reading the result as an object.
     *
     * <p>See {@link #compile(Compilable)} for
     * a similar method that operates through the compilable
     * interface rather than the serializable interface.
     *
     * <p><i>Implementation Note</i>: See the note for {@link
     * #compile(Compilable)}.
     *
     * @param s A serializable object.
     * @return The result of serializing and deserializing the object.
     * @throws IOException If there is an I/O exception serializing or
     * deserializing the object, or if there is a class not found exception
     * thrown while deserializing.
     */
    public static Object serializeDeserialize(Serializable s)
        throws IOException {

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        objOut.writeObject(s);
        ByteArrayInputStream bytesIn
            = new ByteArrayInputStream(bytesOut.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);
        try {
            return objIn.readObject();
        } catch (ClassNotFoundException e) {
            String msg = "Compile i/o class not found exception=" + e;
            throw new IOException(msg);
        }
        // don't need to close the objIn
    }

    public static void main(String args[]){
    	try{
    		return ;
    	}finally{
    		System.out.println("finally executed");
    	}
    }
    
}

