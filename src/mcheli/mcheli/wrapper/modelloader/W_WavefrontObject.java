package mcheli.wrapper.modelloader;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelFormatException;









@SideOnly(Side.CLIENT)
public class W_WavefrontObject
  extends W_ModelCustom
{
  private static Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
  private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
  private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
  private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
  private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
  private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
  private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
  
  private static Pattern groupObjectPattern = Pattern.compile("([go]( [-\\$\\w\\d]+) *\\n)|([go]( [-\\$\\w\\d]+) *$)");
  private static Matcher vertexMatcher;
  private static Matcher vertexNormalMatcher;
  private static Matcher textureCoordinateMatcher;
  private static Matcher face_V_VT_VN_Matcher;
  private static Matcher face_V_VT_Matcher;
  private static Matcher face_V_VN_Matcher; private static Matcher face_V_Matcher; private static Matcher groupObjectMatcher; public ArrayList<W_Vertex> vertices = new ArrayList();
  public ArrayList<W_Vertex> vertexNormals = new ArrayList();
  public ArrayList<W_TextureCoordinate> textureCoordinates = new ArrayList();
  public ArrayList<W_GroupObject> groupObjects = new ArrayList();
  private W_GroupObject currentGroupObject;
  private String fileName;
  
  public W_WavefrontObject(ResourceLocation resource)
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
      throw new ModelFormatException("IO Exception reading model format", e);
    }
  }
  

  public W_WavefrontObject(String fileName, URL resource)
    throws ModelFormatException
  {
    this.fileName = fileName;
    
    try
    {
      loadObjModel(resource.openStream());
    }
    catch (IOException e)
    {
      throw new ModelFormatException("IO Exception reading model format", e);
    }
  }
  
  public W_WavefrontObject(String filename, InputStream inputStream) throws ModelFormatException
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
        
        if ((!currentLine.startsWith("#")) && (currentLine.length() != 0))
        {


          if (currentLine.startsWith("v "))
          {
            W_Vertex vertex = parseVertex(currentLine, lineCount);
            if (vertex != null)
            {
              checkMinMax(vertex);
              this.vertices.add(vertex);
            }
          }
          else if (currentLine.startsWith("vn "))
          {
            W_Vertex vertex = parseVertexNormal(currentLine, lineCount);
            if (vertex != null)
            {
              this.vertexNormals.add(vertex);
            }
          }
          else if (currentLine.startsWith("vt "))
          {
            W_TextureCoordinate textureCoordinate = parseTextureCoordinate(currentLine, lineCount);
            if (textureCoordinate != null)
            {
              this.textureCoordinates.add(textureCoordinate);
            }
          }
          else if (currentLine.startsWith("f "))
          {

            if (this.currentGroupObject == null)
            {
              this.currentGroupObject = new W_GroupObject("Default");
            }
            
            W_Face face = parseFace(currentLine, lineCount);
            
            if (face != null)
            {
              this.currentGroupObject.faces.add(face);
            }
          }
          else if (((currentLine.startsWith("g ") | currentLine.startsWith("o "))) && 
          
            (currentLine.charAt(2) == '$'))
          {
            W_GroupObject group = parseGroupObject(currentLine, lineCount);
            
            if (group != null)
            {
              if (this.currentGroupObject != null)
              {
                this.groupObjects.add(this.currentGroupObject);
              }
            }
            
            this.currentGroupObject = group;
          }
        }
      }
      this.groupObjects.add(this.currentGroupObject); return;
    }
    catch (IOException e)
    {
      throw new ModelFormatException("IO Exception reading model format", e);
    }
    finally
    {
      checkMinMaxFinal();
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
    for (W_GroupObject groupObject : this.groupObjects)
    {
      if (partName.equalsIgnoreCase(groupObject.name))
      {
        groupObject.render();
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
  
  private W_Vertex parseVertex(String line, int lineCount) throws ModelFormatException
  {
    W_Vertex vertex = null;
    
    if (isValidVertexLine(line))
    {
      line = line.substring(line.indexOf(" ") + 1);
      String[] tokens = line.split(" ");
      
      try
      {
        if (tokens.length == 2)
        {
          return new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
        }
        if (tokens.length == 3)
        {
          return new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
        }
      }
      catch (NumberFormatException e)
      {
        throw new ModelFormatException(String.format("Number formatting error at line %d", new Object[] { Integer.valueOf(lineCount) }), e);
      }
    }
    else
    {
      throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
    }
    
    return vertex;
  }
  
  private W_Vertex parseVertexNormal(String line, int lineCount) throws ModelFormatException
  {
    W_Vertex vertexNormal = null;
    
    if (isValidVertexNormalLine(line))
    {
      line = line.substring(line.indexOf(" ") + 1);
      String[] tokens = line.split(" ");
      
      try
      {
        if (tokens.length == 3) {
          return new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
        }
      }
      catch (NumberFormatException e) {
        throw new ModelFormatException(String.format("Number formatting error at line %d", new Object[] { Integer.valueOf(lineCount) }), e);
      }
    }
    else
    {
      throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
    }
    
    return vertexNormal;
  }
  
  private W_TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws ModelFormatException
  {
    W_TextureCoordinate textureCoordinate = null;
    
    if (isValidTextureCoordinateLine(line))
    {
      line = line.substring(line.indexOf(" ") + 1);
      String[] tokens = line.split(" ");
      
      try
      {
        if (tokens.length == 2)
          return new W_TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]));
        if (tokens.length == 3) {
          return new W_TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
        }
      }
      catch (NumberFormatException e) {
        throw new ModelFormatException(String.format("Number formatting error at line %d", new Object[] { Integer.valueOf(lineCount) }), e);
      }
    }
    else
    {
      throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
    }
    
    return textureCoordinate;
  }
  
  private W_Face parseFace(String line, int lineCount) throws ModelFormatException
  {
    W_Face face = null;
    
    if (isValidFaceLine(line))
    {
      face = new W_Face();
      
      String trimmedLine = line.substring(line.indexOf(" ") + 1);
      String[] tokens = trimmedLine.split(" ");
      String[] subTokens = null;
      
      if (tokens.length == 3)
      {
        if (this.currentGroupObject.glDrawingMode == -1)
        {
          this.currentGroupObject.glDrawingMode = 4;
        }
        else if (this.currentGroupObject.glDrawingMode != 4)
        {
          throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + this.fileName + "' - Invalid number of points for face (expected 4, found " + tokens.length + ")");
        }
      }
      else if (tokens.length == 4)
      {
        if (this.currentGroupObject.glDrawingMode == -1)
        {
          this.currentGroupObject.glDrawingMode = 7;
        }
        else if (this.currentGroupObject.glDrawingMode != 7)
        {
          throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + this.fileName + "' - Invalid number of points for face (expected 3, found " + tokens.length + ")");
        }
      }
      

      if (isValidFace_V_VT_VN_Line(line))
      {
        face.vertices = new W_Vertex[tokens.length];
        face.textureCoordinates = new W_TextureCoordinate[tokens.length];
        face.vertexNormals = new W_Vertex[tokens.length];
        
        for (int i = 0; i < tokens.length; i++)
        {
          subTokens = tokens[i].split("/");
          
          face.vertices[i] = ((W_Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1));
          face.textureCoordinates[i] = ((W_TextureCoordinate)this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1));
          face.vertexNormals[i] = ((W_Vertex)this.vertexNormals.get(Integer.parseInt(subTokens[2]) - 1));
        }
        
        face.faceNormal = face.calculateFaceNormal();

      }
      else if (isValidFace_V_VT_Line(line))
      {
        face.vertices = new W_Vertex[tokens.length];
        face.textureCoordinates = new W_TextureCoordinate[tokens.length];
        
        for (int i = 0; i < tokens.length; i++)
        {
          subTokens = tokens[i].split("/");
          
          face.vertices[i] = ((W_Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1));
          face.textureCoordinates[i] = ((W_TextureCoordinate)this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1));
        }
        
        face.faceNormal = face.calculateFaceNormal();

      }
      else if (isValidFace_V_VN_Line(line))
      {
        face.vertices = new W_Vertex[tokens.length];
        face.vertexNormals = new W_Vertex[tokens.length];
        
        for (int i = 0; i < tokens.length; i++)
        {
          subTokens = tokens[i].split("//");
          
          face.vertices[i] = ((W_Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1));
          face.vertexNormals[i] = ((W_Vertex)this.vertexNormals.get(Integer.parseInt(subTokens[1]) - 1));
        }
        
        face.faceNormal = face.calculateFaceNormal();

      }
      else if (isValidFace_V_Line(line))
      {
        face.vertices = new W_Vertex[tokens.length];
        
        for (int i = 0; i < tokens.length; i++)
        {
          face.vertices[i] = ((W_Vertex)this.vertices.get(Integer.parseInt(tokens[i]) - 1));
        }
        
        face.faceNormal = face.calculateFaceNormal();
      }
      else
      {
        throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
      }
    }
    else
    {
      throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
    }
    
    return face;
  }
  
  private W_GroupObject parseGroupObject(String line, int lineCount) throws ModelFormatException
  {
    W_GroupObject group = null;
    
    if (isValidGroupObjectLine(line))
    {
      String trimmedLine = line.substring(line.indexOf(" ") + 1);
      
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
    if (vertexMatcher != null)
    {
      vertexMatcher.reset();
    }
    
    vertexMatcher = vertexPattern.matcher(line);
    return vertexMatcher.matches();
  }
  





  private static boolean isValidVertexNormalLine(String line)
  {
    if (vertexNormalMatcher != null)
    {
      vertexNormalMatcher.reset();
    }
    
    vertexNormalMatcher = vertexNormalPattern.matcher(line);
    return vertexNormalMatcher.matches();
  }
  





  private static boolean isValidTextureCoordinateLine(String line)
  {
    if (textureCoordinateMatcher != null)
    {
      textureCoordinateMatcher.reset();
    }
    
    textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
    return textureCoordinateMatcher.matches();
  }
  





  private static boolean isValidFace_V_VT_VN_Line(String line)
  {
    if (face_V_VT_VN_Matcher != null)
    {
      face_V_VT_VN_Matcher.reset();
    }
    
    face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
    return face_V_VT_VN_Matcher.matches();
  }
  





  private static boolean isValidFace_V_VT_Line(String line)
  {
    if (face_V_VT_Matcher != null)
    {
      face_V_VT_Matcher.reset();
    }
    
    face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
    return face_V_VT_Matcher.matches();
  }
  





  private static boolean isValidFace_V_VN_Line(String line)
  {
    if (face_V_VN_Matcher != null)
    {
      face_V_VN_Matcher.reset();
    }
    
    face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
    return face_V_VN_Matcher.matches();
  }
  





  private static boolean isValidFace_V_Line(String line)
  {
    if (face_V_Matcher != null)
    {
      face_V_Matcher.reset();
    }
    
    face_V_Matcher = face_V_Pattern.matcher(line);
    return face_V_Matcher.matches();
  }
  





  private static boolean isValidFaceLine(String line)
  {
    return (isValidFace_V_VT_VN_Line(line)) || (isValidFace_V_VT_Line(line)) || (isValidFace_V_VN_Line(line)) || (isValidFace_V_Line(line));
  }
  





  private static boolean isValidGroupObjectLine(String line)
  {
    if (groupObjectMatcher != null)
    {
      groupObjectMatcher.reset();
    }
    
    groupObjectMatcher = groupObjectPattern.matcher(line);
    return groupObjectMatcher.matches();
  }
  

  public String getType()
  {
    return "obj";
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
    return this.vertices.size();
  }
  

  public int getFaceNum()
  {
    return getVertexNum() / 3;
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
