package mcheli.wrapper.modelloader;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelFormatException;










@SideOnly(Side.CLIENT)
public class W_MetasequoiaObject
  extends W_ModelCustom
{
  public ArrayList<W_Vertex> vertices = new ArrayList();
  public ArrayList<W_GroupObject> groupObjects = new ArrayList();
  private W_GroupObject currentGroupObject = null;
  private String fileName;
  private int vertexNum = 0;
  private int faceNum = 0;
  
  public W_MetasequoiaObject(ResourceLocation resource)
    throws ModelFormatException
  {
    this.fileName = resource.toString();
    
    try
    {
      IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);
      loadObjModel(res.getInputStream());
    }
    catch (IOException e)
    {
      throw new ModelFormatException("IO Exception reading model format:" + this.fileName, e);
    }
  }
  

  public W_MetasequoiaObject(String fileName, URL resource)
    throws ModelFormatException
  {
    this.fileName = fileName;
    
    try
    {
      loadObjModel(resource.openStream());
    }
    catch (IOException e)
    {
      throw new ModelFormatException("IO Exception reading model format:" + this.fileName, e);
    }
  }
  
  public W_MetasequoiaObject(String filename, InputStream inputStream) throws ModelFormatException
  {
    this.fileName = filename;
    loadObjModel(inputStream);
  }
  
  public boolean containsPart(String partName)
  {
    for (W_GroupObject groupObject : this.groupObjects)
    {
      if (partName.equalsIgnoreCase(groupObject.name))
      {
        return true;
      }
    }
    return false;
  }
  
  private void loadObjModel(InputStream inputStream) throws ModelFormatException
  {
    BufferedReader reader = null;
    
    String currentLine = null;
    int lineCount = 0;
    
    try
    {
      reader = new BufferedReader(new InputStreamReader(inputStream));
      
      while ((currentLine = reader.readLine()) != null)
      {
        lineCount++;
        currentLine = currentLine.replaceAll("\\s+", " ").trim();
        

        if (isValidGroupObjectLine(currentLine))
        {
          W_GroupObject group = parseGroupObject(currentLine, lineCount);
          if (group != null)
          {



            group.glDrawingMode = 4;
            
            this.vertices.clear();
            int vertexNum = 0;
            
            boolean mirror = false;
            
            double facet = Math.cos(0.785398163375D);
            boolean shading = false;
            

            while ((currentLine = reader.readLine()) != null)
            {
              lineCount++;
              currentLine = currentLine.replaceAll("\\s+", " ").trim();
              
              if (currentLine.equalsIgnoreCase("mirror 1"))
              {
                mirror = true;
              }
              if (currentLine.equalsIgnoreCase("shading 1"))
              {
                shading = true;
              }
              
              String[] s = currentLine.split(" ");
              if ((s.length == 2) && (s[0].equalsIgnoreCase("facet")))
              {
                facet = Math.cos(Double.parseDouble(s[1]) * 3.1415926535D / 180.0D);
              }
              
              if (isValidVertexLine(currentLine))
              {
                vertexNum = Integer.valueOf(currentLine.split(" ")[1]).intValue();
                break;
              }
            }
            

            if (vertexNum > 0)
            {
            	System.out.println(reader.readLine());
              while ((currentLine = reader.readLine()) != null)
              {
            	  
                lineCount++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                
                String[] s = currentLine.split(" ");
                if (s.length == 3)
                {
                  W_Vertex v = new W_Vertex(Float.valueOf(s[0]).floatValue() / 100.0F, Float.valueOf(s[1]).floatValue() / 100.0F, Float.valueOf(s[2]).floatValue() / 100.0F);
                  


                  checkMinMax(v);
                  this.vertices.add(v);
                  
                  vertexNum--;
                  
                  if (vertexNum <= 0) {
                    break;
                  }
                  
                }
                else if (s.length > 0)
                {
                  throw new ModelFormatException("format error : " + this.fileName + " : line=" + lineCount);
                }
              }
              
              int faceNum = 0;
              
              while ((currentLine = reader.readLine()) != null)
              {
                lineCount++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                
                if (isValidFaceLine(currentLine))
                {
                  faceNum = Integer.valueOf(currentLine.split(" ")[1]).intValue();
                }
              }
              
     
              if (faceNum > 0)
              {
            	  
                while ((currentLine = reader.readLine()) != null)
                {
                  lineCount++;
                  currentLine = currentLine.replaceAll("\\s+", " ").trim();
                 
                  String[] s = currentLine.split(" ");
                  if (s.length > 2)
                  {
                    if (Integer.valueOf(s[0]).intValue() >= 3)
                    {
                    	
                      W_Face[] faces = parseFace(currentLine, lineCount, mirror);
                      for (W_Face face : faces)
                      {
                        group.faces.add(face);
                        System.out.println("face");
                      }
                    }
                    faceNum--;
                    if (faceNum <= 0) {
                      break;
                    }
                    
                  }
                  else if ((s.length > 2) && (Integer.valueOf(s[0]).intValue() != 3))
                  {
                    throw new ModelFormatException("found face is not triangle : " + this.fileName + " : line=" + lineCount);
                  }
                }
                
                calcVerticesNormal(group, shading, facet);
              }
            }
            this.vertexNum += this.vertices.size();
            this.faceNum += group.faces.size();
            this.vertices.clear();
            
            this.groupObjects.add(group);
          }
        }
      }
      return;
    } catch (IOException e) {
      throw new ModelFormatException("IO Exception reading model format : " + this.fileName, e);
    }
    finally
    {
      checkMinMaxFinal();
      this.vertices = null;
      try
      {
        reader.close();
      }
      catch (IOException e) {}
      



      try
      {
        inputStream.close();
      }
      catch (IOException e) {}
    }
  }
  




  public void calcVerticesNormal(W_GroupObject group, boolean shading, double facet)
  {
    for (W_Face f : group.faces)
    {
      f.vertexNormals = new W_Vertex[f.verticesID.length];
      for (int i = 0; i < f.verticesID.length; i++)
      {
        W_Vertex vn = getVerticesNormalFromFace(f.faceNormal, f.verticesID[i], group, (float)facet);
        vn.normalize();
        







        if (shading)
        {
          if (f.faceNormal.x * vn.x + f.faceNormal.y * vn.y + f.faceNormal.z * vn.z >= facet)
          {
            f.vertexNormals[i] = vn;
          }
          else
          {
            f.vertexNormals[i] = f.faceNormal;
          }
          
        }
        else {
          f.vertexNormals[i] = f.faceNormal;
        }
      }
    }
  }
  
  public W_Vertex getVerticesNormalFromFace(W_Vertex faceNormal, int verticesID, W_GroupObject group, float facet)
  {
    W_Vertex v = new W_Vertex(0.0F, 0.0F, 0.0F);
    
    for (W_Face f : group.faces)
    {
      for (int id : f.verticesID)
      {
        if (id == verticesID)
        {
          if (f.faceNormal.x * faceNormal.x + f.faceNormal.y * faceNormal.y + f.faceNormal.z * faceNormal.z < facet)
            break;
          v.add(f.faceNormal); break;
        }
      }
    }
    


    v.normalize();
    
    return v;
  }
  


  public void renderAll()
  {
    Tessellator tessellator = Tessellator.instance;
    
    if (this.currentGroupObject != null)
    {
      tessellator.startDrawing(this.currentGroupObject.glDrawingMode);
    }
    else
    {
      tessellator.startDrawing(4);
    }
    tessellateAll(tessellator);
    
    tessellator.draw();
  }
  
  public void tessellateAll(Tessellator tessellator)
  {
    for (W_GroupObject groupObject : this.groupObjects)
    {
      groupObject.render(tessellator);
    }
  }
  

  public void renderOnly(String... groupNames)
  {
    for (W_GroupObject groupObject : this.groupObjects)
    {
      for (String groupName : groupNames)
      {
        if (groupName.equalsIgnoreCase(groupObject.name))
        {
          groupObject.render();
        }
      }
    }
  }
  
  public void tessellateOnly(Tessellator tessellator, String... groupNames) {
    for (W_GroupObject groupObject : this.groupObjects)
    {
      for (String groupName : groupNames)
      {
        if (groupName.equalsIgnoreCase(groupObject.name))
        {
          groupObject.render(tessellator);
        }
      }
    }
  }
  

  public void renderPart(String partName)
  {
    if (partName.charAt(0) == '$')
    {
      for (int i = 0; i < this.groupObjects.size(); i++)
      {
        W_GroupObject groupObject = (W_GroupObject)this.groupObjects.get(i);
        if (partName.equalsIgnoreCase(groupObject.name))
        {
          groupObject.render();
          
          i++;
          for (; i < this.groupObjects.size(); i++)
          {
            groupObject = (W_GroupObject)this.groupObjects.get(i);
            if (groupObject.name.charAt(0) == '$') {
              break;
            }
            
            groupObject.render();
          }
          
        }
        
      }
    } else {
      for (W_GroupObject groupObject : this.groupObjects)
      {
        if (partName.equalsIgnoreCase(groupObject.name))
        {
          groupObject.render();
        }
      }
    }
  }
  
  public void tessellatePart(Tessellator tessellator, String partName) {
    for (W_GroupObject groupObject : this.groupObjects)
    {
      if (partName.equalsIgnoreCase(groupObject.name))
      {
        groupObject.render(tessellator);
      }
    }
  }
  

  public void renderAllExcept(String... excludedGroupNames)
  {
    for (W_GroupObject groupObject : this.groupObjects)
    {
      boolean skipPart = false;
      for (String excludedGroupName : excludedGroupNames)
      {
        if (excludedGroupName.equalsIgnoreCase(groupObject.name))
        {
          skipPart = true;
        }
      }
      if (!skipPart)
      {
        groupObject.render();
      }
    }
  }
  

  public void tessellateAllExcept(Tessellator tessellator, String... excludedGroupNames)
  {
    for (W_GroupObject groupObject : this.groupObjects)
    {
      boolean exclude = false;
      for (String excludedGroupName : excludedGroupNames)
      {
        if (excludedGroupName.equalsIgnoreCase(groupObject.name))
        {
          exclude = true;
        }
      }
      if (!exclude)
      {
        groupObject.render(tessellator);
      }
    }
  }
  
  private W_Face[] parseFace(String line, int lineCount, boolean mirror)
  {
    String[] s = line.split("[ VU)(M]+");
    

    


    int vnum = Integer.valueOf(s[0]).intValue();
    if ((vnum != 3) && (vnum != 4))
    {
      return new W_Face[0];
    }
    
    if (vnum == 3)
    {
      W_Face face = new W_Face();
      face.verticesID = new int[] { Integer.valueOf(s[3]).intValue(), Integer.valueOf(s[2]).intValue(), Integer.valueOf(s[1]).intValue() };
      





      face.vertices = new W_Vertex[] { (W_Vertex)this.vertices.get(face.verticesID[0]), (W_Vertex)this.vertices.get(face.verticesID[1]), (W_Vertex)this.vertices.get(face.verticesID[2]) };
      



      if (s.length >= 11)
      {
        face.textureCoordinates = new W_TextureCoordinate[] { new W_TextureCoordinate(Float.valueOf(s[9]).floatValue(), Float.valueOf(s[10]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[7]).floatValue(), Float.valueOf(s[8]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[5]).floatValue(), Float.valueOf(s[6]).floatValue()) };


      }
      else
      {


        face.textureCoordinates = new W_TextureCoordinate[] { new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F) };
      }
      



      face.faceNormal = face.calculateFaceNormal();
      
      return new W_Face[] { face };
    }
    

    W_Face face1 = new W_Face();
    face1.verticesID = new int[] { Integer.valueOf(s[3]).intValue(), Integer.valueOf(s[2]).intValue(), Integer.valueOf(s[1]).intValue() };
    





    face1.vertices = new W_Vertex[] { (W_Vertex)this.vertices.get(face1.verticesID[0]), (W_Vertex)this.vertices.get(face1.verticesID[1]), (W_Vertex)this.vertices.get(face1.verticesID[2]) };
    




    if (s.length >= 12)
    {
      face1.textureCoordinates = new W_TextureCoordinate[] { new W_TextureCoordinate(Float.valueOf(s[10]).floatValue(), Float.valueOf(s[11]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[8]).floatValue(), Float.valueOf(s[9]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[6]).floatValue(), Float.valueOf(s[7]).floatValue()) };


    }
    else
    {


      face1.textureCoordinates = new W_TextureCoordinate[] { new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F) };
    }
    




    face1.faceNormal = face1.calculateFaceNormal();
    

    W_Face face2 = new W_Face();
    face2.verticesID = new int[] { Integer.valueOf(s[4]).intValue(), Integer.valueOf(s[3]).intValue(), Integer.valueOf(s[1]).intValue() };
    





    face2.vertices = new W_Vertex[] { (W_Vertex)this.vertices.get(face2.verticesID[0]), (W_Vertex)this.vertices.get(face2.verticesID[1]), (W_Vertex)this.vertices.get(face2.verticesID[2]) };
    




    if (s.length >= 14)
    {
      face2.textureCoordinates = new W_TextureCoordinate[] { new W_TextureCoordinate(Float.valueOf(s[12]).floatValue(), Float.valueOf(s[13]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[10]).floatValue(), Float.valueOf(s[11]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[6]).floatValue(), Float.valueOf(s[7]).floatValue()) };


    }
    else
    {


      face2.textureCoordinates = new W_TextureCoordinate[] { new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F) };
    }
    



    face2.faceNormal = face2.calculateFaceNormal();
    
    return new W_Face[] { face1, face2 };
  }
  



  private static boolean isValidGroupObjectLine(String line)
  {
    String[] s = line.split(" ");
    
    if ((s.length < 2) || (!s[0].equals("Object")))
    {
      return false;
    }
    
    if ((s[1].length() < 4) || (s[1].charAt(0) != '"'))
    {
      return false;
    }
    
    return true;
  }
  
  private W_GroupObject parseGroupObject(String line, int lineCount) throws ModelFormatException {
    W_GroupObject group = null;
    
    if (isValidGroupObjectLine(line))
    {
      String[] s = line.split(" ");
      String trimmedLine = s[1].substring(1, s[1].length() - 1);
      
      if (trimmedLine.length() > 0)
      {
        group = new W_GroupObject(trimmedLine);
      }
    }
    else
    {
      throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
    }
    
    return group;
  }
  

  private static boolean isValidVertexLine(String line)
  {
    String[] s = line.split(" ");
    
    if (!s[0].equals("vertex")) { return false;
    }
    return true;
  }
  





  private static boolean isValidFaceLine(String line)
  {
    String[] s = line.split(" ");
    
    if (!s[0].equals("face")) { return false;
    }
    return true;
  }
  

  public String getType()
  {
    return "mqo";
  }
  


  public void renderAllLine(int startLine, int maxLine)
  {
    Tessellator tessellator = Tessellator.instance;
    
    tessellator.startDrawing(1);
    
    renderAllLine(tessellator, startLine, maxLine);
    
    tessellator.draw();
  }
  
  public void renderAllLine(Tessellator tessellator, int startLine, int maxLine)
  {
    int lineCnt = 0;
    for (W_GroupObject groupObject : this.groupObjects)
    {
      if (groupObject.faces.size() > 0)
      {
        for (W_Face face : groupObject.faces)
        {
          for (int i = 0; i < face.vertices.length / 3; i++)
          {
            W_Vertex v1 = face.vertices[(i * 3 + 0)];
            W_Vertex v2 = face.vertices[(i * 3 + 1)];
            W_Vertex v3 = face.vertices[(i * 3 + 2)];
            
            lineCnt++;
            if (lineCnt > maxLine) return;
            tessellator.addVertex(v1.x, v1.y, v1.z);
            tessellator.addVertex(v2.x, v2.y, v2.z);
            
            lineCnt++;
            if (lineCnt > maxLine) return;
            tessellator.addVertex(v2.x, v2.y, v2.z);
            tessellator.addVertex(v3.x, v3.y, v3.z);
            
            lineCnt++;
            if (lineCnt > maxLine) return;
            tessellator.addVertex(v3.x, v3.y, v3.z);
            tessellator.addVertex(v1.x, v1.y, v1.z);
          }
        }
      }
    }
  }
  

  public int getVertexNum()
  {
    return this.vertexNum;
  }
  

  public int getFaceNum()
  {
    return this.faceNum;
  }
  

  public void renderAll(int startFace, int maxFace)
  {
    if (startFace < 0) { startFace = 0;
    }
    Tessellator tessellator = Tessellator.instance;
    
    tessellator.startDrawing(4);
    
    renderAll(tessellator, startFace, maxFace);
    
    tessellator.draw();
  }
  
  public void renderAll(Tessellator tessellator, int startFace, int maxLine)
  {
    int faceCnt = 0;
    for (W_GroupObject groupObject : this.groupObjects)
    {
      if (groupObject.faces.size() > 0)
      {
        for (W_Face face : groupObject.faces)
        {
          faceCnt++;
          if (faceCnt >= startFace) {
            if (faceCnt > maxLine) return;
            face.addFaceForRender(tessellator);
          }
        }
      }
    }
  }
}
