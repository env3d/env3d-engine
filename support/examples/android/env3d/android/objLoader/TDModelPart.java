package env3d.android.objLoader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import android.util.Log;

public class TDModelPart {
	Vector<Short> faces;
	Vector<Short> vtPointer;
	Vector<Short> vnPointer;
	Material material;
	private FloatBuffer normalBuffer, texBuffer;
	ShortBuffer faceBuffer;
	
	public TDModelPart(Vector<Short> faces, Vector<Short> vtPointer,
			Vector<Short> vnPointer, Material material, Vector<Float> vn, Vector<Float> vt) {
		super();
		this.faces = faces;
		this.vtPointer = vtPointer;
		this.vnPointer = vnPointer;
		this.material = material;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vnPointer.size() * 4*3);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		for(int i=0; i<vnPointer.size(); i++){
			float x=vn.get(vnPointer.get(i)*3);
			float y=vn.get(vnPointer.get(i)*3+1);
			float z=vn.get(vnPointer.get(i)*3+2);
			normalBuffer.put(x);
			normalBuffer.put(y);
			normalBuffer.put(z);
		}
		normalBuffer.position(0);

		float [] texArray = new float[vtPointer.size()*2];
		for (int i = 0; i<vtPointer.size(); i++) {
			texArray[faces.get(i)*2] = vt.get(vtPointer.get(i)*2);			
			texArray[faces.get(i)*2+1] = vt.get(vtPointer.get(i)*2+1);			
		}
		for (short s : faces) {
			Log.v("faces coord",s+"");
		}
		for (float f : texArray) {
			Log.v("texture coord",f+"");
		}
		ByteBuffer tBuf = ByteBuffer.allocateDirect(vtPointer.size() * 4*2);
		tBuf.order(ByteOrder.nativeOrder());
		texBuffer = tBuf.asFloatBuffer();
		texBuffer.put(texArray);
//		for(int i=0; i<vtPointer.size(); i++){					
//			float x=vt.get(vtPointer.get(i)*2);
//			float y=vt.get(vtPointer.get(i)*2+1);			
//			texBuffer.put(y);
//			texBuffer.put(x);
//		}
		texBuffer.position(0);

		ByteBuffer fBuf = ByteBuffer.allocateDirect(faces.size() * 2);
		fBuf.order(ByteOrder.nativeOrder());
		faceBuffer = fBuf.asShortBuffer();
		faceBuffer.put(toPrimitiveArrayS(faces));
		faceBuffer.position(0);
	}
	@Override
	public String toString(){
		String str=new String();
		if(material!=null)
			str+="Material name:"+material.getName();
		else
			str+="Material not defined!";
		str+="\nNumber of faces:"+faces.size();
		str+="\nNumber of vnPointers:"+vnPointer.size();
		str+="\nNumber of vtPointers:"+vtPointer.size();
		return str;
	}
	public ShortBuffer getFaceBuffer(){
		return faceBuffer;
	}
	public FloatBuffer getNormalBuffer(){
		return normalBuffer;
	}
	public FloatBuffer getTexBuffer(){
		return texBuffer;
	}
	
	private static short[] toPrimitiveArrayS(Vector<Short> vector){
		short[] s;
		s=new short[vector.size()];
		for (int i=0; i<vector.size(); i++){
			s[i]=vector.get(i);
		}
		return s;
	}
	public int getFacesCount(){
		return faces.size();
	}
	public Material getMaterial(){
		return material;
	}
	
	
}
