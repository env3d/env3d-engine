package env3d.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.content.res.AssetManager;

public class ObjImporter {
	private static final String SPACE = " ";
	private static final String SLASH = "/";
	private static HashMap<String, ModelData3D> modelCache = new HashMap<String, ModelData3D>();

	public static ModelData3D importObj(AssetManager asset, String fileName) throws Exception {
		
		if (modelCache.get(fileName)  != null) {
			return modelCache.get(fileName);
		}
		
		InputStream in = asset.open(fileName);
		
		ModelData3D data = new ModelData3D();
		ArrayList<VertexF> verticies = new ArrayList<VertexF>();
		ArrayList<UVCoord> uvs = new ArrayList<UVCoord>();
		ArrayList<Face> faces = new ArrayList<Face>();
		// 1) read in verticies,
		// 2) read in uvs
		// 3) create faces which are verticies and uvs expanded
		// 4) unroll faces into ModelData3D using sequential indicies
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringTokenizer st;
		String line = reader.readLine();
		System.out.println("Loading obj data");
		while (line != null) {
			st = new StringTokenizer(line, SPACE);
			if (st.countTokens() > 1) {
				String lineType = st.nextToken(SPACE);
				if (lineType.equals("v")) {
					// vertex
					VertexF vert = new VertexF();
					vert.x = Float.valueOf(st.nextToken());
					vert.y = Float.valueOf(st.nextToken());
					vert.z = Float.valueOf(st.nextToken());
					verticies.add(vert);
				} else if (lineType.equals("vt")) {
					// texture mapping
					UVCoord uv = new UVCoord();
					uv.u = Float.valueOf(st.nextToken());
					uv.v = Float.valueOf(st.nextToken());
					uvs.add(uv);
				} else if (lineType.equals("f")) {
					// face					
					Face face = new Face();
					face.v1 = verticies.get(Integer.valueOf(st.nextToken(SLASH)
							.trim()) - 1);
					face.uv1 = uvs.get(Integer.valueOf(st.nextToken(SLASH)
							.trim()) - 1);
					// Ignore the normal
					st.nextToken(SPACE);
					face.v2 = verticies.get(Integer.valueOf(st.nextToken(SLASH)
							.trim()) - 1);
					face.uv2 = uvs.get(Integer.valueOf(st.nextToken(SLASH)
							.trim()) - 1);
					// Ignore the normal
					st.nextToken(SPACE);
					face.v3 = verticies.get(Integer.valueOf(st.nextToken(SLASH)
							.trim()) - 1);
					face.uv3 = uvs.get(Integer.valueOf(st.nextToken(SLASH)
							.trim()) - 1);
					// Ignore the normal
					st.nextToken(SPACE);
					faces.add(face);
				}
			}
			line = reader.readLine();
		}
		// printFaces(faces);
		int facesSize = faces.size();
		System.out.println(facesSize + " polys");
		data.vertexCount = facesSize * 3;
		data.vertices = new int[facesSize * 3 * 3];
		data.tex = new float[facesSize * 3 * 2];
		data.indices = new byte[facesSize * 3];
		for (int i = 0; i < facesSize; i++) {
			Face face = faces.get(i);
			data.vertices[i * 9] = toFP(face.v1.x);
			data.vertices[i * 9 + 1] = toFP(face.v1.y);
			data.vertices[i * 9 + 2] = toFP(face.v1.z);
			data.vertices[i * 9 + 3] = toFP(face.v2.x);
			data.vertices[i * 9 + 4] = toFP(face.v2.y);
			data.vertices[i * 9 + 5] = toFP(face.v2.z);
			data.vertices[i * 9 + 6] = toFP(face.v3.x);
			data.vertices[i * 9 + 7] = toFP(face.v3.y);
			data.vertices[i * 9 + 8] = toFP(face.v3.z);
			data.tex[i * 6] = face.uv1.u;
			data.tex[i * 6 + 1] = face.uv1.v;
			data.tex[i * 6 + 2] = face.uv2.u;
			data.tex[i * 6 + 3] = face.uv2.v;
			data.tex[i * 6 + 4] = face.uv3.u;
			data.tex[i * 6 + 5] = face.uv3.v;
			data.indices[i * 3] = (byte) (i * 3);
			data.indices[i * 3 + 1] = (byte) (i * 3 + 1);
			data.indices[i * 3 + 2] = (byte) (i * 3 + 2);
		}
		reader.close();
		data.init();
		//printFaces(faces);
		modelCache.put(fileName, data);
		return data;
	}

	private static int toFP(float f) {
		// normally you'd << 16 but um, can't do that with a float so we
		// multiply by 16 bits.
		return (int) ((double) f * 0x10000);
	}

	private static void printFaces(ArrayList<Face> faces) {
		for (Face f : faces) {
			System.out.println("Face uv1 " + f.uv1.u + " " + f.uv1.v);
			System.out.println("Face uv2 " + f.uv2.u + " " + f.uv2.v);
			System.out.println("Face uv3 " + f.uv3.u + " " + f.uv3.v);
			System.out.println("Face v1 " + f.v1.x + " " + f.v1.y + " "
					+ f.v1.z);
			System.out.println("Face v2 " + f.v2.x + " " + f.v2.y + " "
					+ f.v2.z);
			System.out.println("Face v3 " + f.v3.x + " " + f.v3.y + " "
					+ f.v3.z);
		}
	}

	private static class VertexF {
		public float x;
		public float y;
		public float z;
	}

	private static class UVCoord {
		public float u;
		public float v;
	}

	private static class Face {
		public UVCoord uv1, uv2, uv3;
		public VertexF v1, v2, v3;
	}

}