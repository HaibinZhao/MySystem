package Xproer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import org.apache.commons.fileupload.FileItem;

/**
 * �ϴ�����
 * ���¼�¼��
 * 	2013-01-25 ����
 *
 */
public class Uploader {
	
	public PageContext m_pc;
	String m_folder;		//�ϴ��ļ��С�D:\\webapps\\WordPaster\\upload\\
	String m_curBasePath;	//��ǰ�ļ�·����http://localhost:8080/WordPaster/
	String m_filePathRel;	//�ļ��ڷ������е����·����http://localhost:8080/WordPaster/upload/2012/05/24/
	String m_fileName;		//�ļ����ơ�11223344.png
	
	/*
	 * ��JSPҳ���й��졣���� pageContext
	 * */
	public Uploader(PageContext pc,HttpServletRequest sr)
	{
		this.m_pc = pc;
		String path = sr.getContextPath();
		this.m_curBasePath = sr.getScheme()+"://" + sr.getServerName()+":" + sr.getServerPort() + path+"/";
	}

	/*
	 * ��ȡ�ļ����·����
	 * ����ֵ��
	 * 		http://localhost:8080/WordPaster/upload/2012/05/24/11223344.png
	 * */
	public String GetFilePathRel()
	{
		return this.m_filePathRel + this.m_fileName;
	}
	
	/*
	 * �����ϴ��ļ���
	 * 2012\\05\\24\\
	 * */
	public void CreateFolder()
	{
		Date timeCur = new Date();
		SimpleDateFormat fmtYY = new SimpleDateFormat("yyyy");
		SimpleDateFormat fmtMM = new SimpleDateFormat("MM");
		SimpleDateFormat fmtDD = new SimpleDateFormat("dd");
		String strYY = fmtYY.format(timeCur);
		String strMM = fmtMM.format(timeCur);
		String strDD = fmtDD.format(timeCur);
		
		//���·��/2012/05/24/
		String pathRel = "upload/" + strYY + "/" + strMM + "/" + strDD + "/";
		String pathAbs = "upload\\" + strYY + "\\" + strMM + "\\" + strDD + "\\";
		//�ļ�·��
		this.m_filePathRel = this.m_curBasePath + pathRel;
		
		this.m_folder = this.m_pc.getServletContext().getRealPath("/") + pathAbs;
		
		File f = new File(this.m_folder);
		//�ļ��в�����
		if(!f.exists())
		{
			f.mkdirs();
		}		
	}
	
	/*
	 * ���ݵ�ǰʱ�������ļ����ơ�
	 * ����ֵ��
	 * 		�����գ�ʱ����
	 * 		2012-05-24-16-06
	 * */
	public String GenerateFileName()
	{
		Date timeCur = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("HHmmssSSSS");
		String timeStr = fmt.format(timeCur);
		return timeStr;
	}
	
	//���ļ����浽��������
	public void SaveFile(FileItem upFile) throws IOException
	{
		//11223344.png
		String fileName = upFile.getName();
		//����ؼ�����UTF-8���뷽ʽ�ύ��������ʹ������ķ�ʽ���ļ����ƽ��н��롣
		fileName = fileName.replaceAll("\\+","%20");
		//�ͻ���ʹ�õ���encodeURIComponent����
		fileName = URLDecoder.decode(fileName,"UTF-8");
		
		int pos = fileName.indexOf('.');
		String ext = fileName.substring(pos);
		ext.toLowerCase();
		this.m_fileName = this.GenerateFileName() + ext;
		
		this.CreateFolder();
		String filePath = this.m_folder + this.m_fileName;		

		InputStream stream = upFile.getInputStream();			
		byte[] data = new byte[(int)upFile.getSize()];//128k
		int readLen = stream.read(data);//ʵ�ʶ�ȡ�Ĵ�С
		stream.close();
		
		RandomAccessFile raf = new RandomAccessFile(filePath,"rw");
		//��λ�ļ�λ��
		raf.write(data);
		raf.close();

		
	}

}