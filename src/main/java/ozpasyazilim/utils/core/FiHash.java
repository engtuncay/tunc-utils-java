package ozpasyazilim.utils.core;

import java.io.ByteArrayOutputStream;

import java.math.BigInteger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.StringTokenizer;

public class FiHash
{
  public FiHash()
  {
  }


  private static String getString(byte[] bytes)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < bytes.length; i++)
    {
      byte b = bytes[i];
      sb.append((int) (0x00FF & b));
      if (i + 1 < bytes.length)
      {
        sb.append("-");
      }
    }
    return sb.toString();
  }

  private static byte[] getBytes(String str)
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StringTokenizer st = new StringTokenizer(str, "-", false);
    while (st.hasMoreTokens())
    {
      int i = Integer.parseInt(st.nextToken());
      bos.write((byte) i);
    }
    return bos.toByteArray();
  }

  public static String md5(String source)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] bytes = md.digest(source.getBytes());
      BigInteger number = new BigInteger(1,bytes);
      return number.toString(16);
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e);
    }
  }
}
