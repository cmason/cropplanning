package CPS.Module;

public abstract class CPSModule {

   protected String ModuleName;
   protected String ModuleType;
   protected String ModuleVersion;
   
   public String getModuleName()    { return ModuleName; }
   public String getModuleType()    { return ModuleType; }
   public String getModuleVersion() { return ModuleVersion; }
   // public abstract String[] getRequires();
   
   protected void setModuleName(    String s ) { ModuleName    = s; }
   protected void setModuleType(    String s ) { ModuleType    = s; }
   protected void setModuleVersion( String s ) { ModuleVersion = s; }

   public boolean verifyVersion( String ver ) {
      return getModuleVersion().equalsIgnoreCase( ver );
   }
    
}
