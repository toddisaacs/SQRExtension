package teisaacs.sqrext.model;

import oracle.ide.config.ChangeEventSource;

import oracle.javatools.util.Copyable;

public class SqrEnvData   extends ChangeEventSource
        implements Copyable{
        
    public static final String KEY = "teisaacsSQREXTSettings";
    
    private String env;
    private String server;
    private String userid;
    private String password;
    private String dir1;
    private String dir2;
    private String copyDir1;
    private String copyDir2;
    
    public SqrEnvData() {
    }

    public Object copyTo(Object object) {
        final SqrEnvData copy =
           (object!=null?(SqrEnvData)object:new SqrEnvData());
        copyToImpl(copy);
        return copy;
    }
    
    protected final void copyToImpl(SqrEnvData copy)
    {
      copy.env  = env;
      copy.server = server;
      copy.userid = userid;
      copy.password = password;
      copy.dir1 = dir1;
      copy.dir2 = dir2;
      copy.copyDir1 = copyDir1;
      copy.copyDir2 = copyDir2;
      
      copy.fireChangeEvent();
    }
    
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDir1() {
        return dir1;
    }

    public void setDir1(String dir1) {
        this.dir1 = dir1;
    }

    public String getDir2() {
        return dir2;
    }

    public void setDir2(String dir2) {
        this.dir2 = dir2;
    }

    public String getCopyDir1() {
        return copyDir1;
    }

    public void setCopyDir1(String copyDir1) {
        this.copyDir1 = copyDir1;
    }

    public String getCopyDir2() {
        return copyDir2;
    }

    public void setCopyDir2(String copyDir2) {
        this.copyDir2 = copyDir2;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
