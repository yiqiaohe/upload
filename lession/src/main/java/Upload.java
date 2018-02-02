import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.aliyun.oss.OSSClient;
/**
 * 上传多个文件
 * @author YQH
 *
 */
@WebServlet("/web")
public class Upload extends HttpServlet{

	@Override
        protected void doGet(HttpServletRequest requset, HttpServletResponse response)
                        throws ServletException, IOException {
	        
	        this.doPost(requset, response);
        }

	@Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
	       
		//创建文件解析对象
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        //创建上传文件实例
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        upload.setHeaderEncoding("utf-8");
	        String userName = null;
	        try {
	            //解析request为fileitem，因为支持多文件上传
	            List<?> list = upload.parseRequest(request);
	            //进行迭代
	            Iterator<?> it = list.iterator();
	            while(it.hasNext()){
	                //获取文件名
	                FileItem item = (FileItem) it.next();
	                String fileName = item.getName();
	                if(item.isFormField()){//普通字段
	                	userName = item.getString("UTF-8");
	                }else{
	                	if(fileName != null) {
	    	                    //获得文件输入流
	    	                    InputStream is = item.getInputStream();
	    	                    String endpoint = "oss-cn-beijing.aliyuncs.com";
	    	                    // 云账号AccessKey
	    	                    String accessKeyId = "LTAIYPpBniMvpA2A";
	    	                    String accessKeySecret = "GFQiSYNaBWkVl4xNt09XKoIE0zdcLE";
	    	                    //随机生成文件名
	    	                    fileName = UUID.randomUUID().toString() + fileName.substring(fileName.indexOf("."));
	    	                    // 创建OSSClient实例
	    	                    OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	    	                    ossClient.putObject("file-oss1", fileName, is);
	    	                }
	                }
	            }
	            response.setContentType("text/html;charset=UTF-8");
	            response.getWriter().println(userName + ":文件上传成功！<br/>");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
        }
}
