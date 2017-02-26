package env3d.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

public class ModelData3D {
	public int[] vertices;
	public float[] tex;
	public byte[] indices;
	public int vertexCount;
	
	private IntBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	private ByteBuffer mIndexBuffer;

	public void print() {
		System.out.println("vertices=" + Arrays.toString(vertices));
		System.out.println("tex=" + Arrays.toString(tex));
		System.out.println("indices=" + Arrays.toString(indices));
	}
	public void init() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        
        // flip the texture coordinates to be upside down
        for (int i=0; i<tex.length; i++) {
        	if (i % 2 == 1)
        		tex[i] = 1-tex[i];
        }
        ByteBuffer tbb = ByteBuffer.allocateDirect(tex.length*4);
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asFloatBuffer();
        mTextureBuffer.put(tex);
        mTextureBuffer.position(0);
        //mTextureBuffer.flip();
        

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);		        		
        
	}
	
	public void draw(GL10 gl) {		
		gl.glFrontFace(GL10.GL_CCW);		
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //gl.glDrawElements(GL10.GL_TRIANGLES, vertexCount, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertexCount);
	}
	
	
}