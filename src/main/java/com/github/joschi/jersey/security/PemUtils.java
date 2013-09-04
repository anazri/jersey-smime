package com.github.joschi.jersey.security;

import org.jboss.resteasy.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * Utility classes to extract PublicKey, PrivateKey, and X509Certificate from openssl generated PEM files
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class PemUtils
{
   static
   {
      BouncyIntegration.init();
   }
   public static X509Certificate decodeCertificate(InputStream is) throws Exception
   {
      byte[] der = pemToDer(is);
      ByteArrayInputStream bis = new ByteArrayInputStream(der);
      return DerUtils.decodeCertificate(bis);
   }

   /**
    * Extract a public key from a PEM string
    *
    * @param pem
    * @return
    * @throws Exception
    */
   public static PublicKey decodePublicKey(String pem) throws Exception
   {
      byte[] der = pemToDer(pem);
      return DerUtils.decodePublicKey(der);
   }

   /**
    * Extract a private key that is a PKCS8 pem string (base64 encoded PKCS8)
    *
    * @param pem
    * @return
    * @throws Exception
    */
   public static PrivateKey decodePrivateKey(String pem) throws Exception
   {
      byte[] der = pemToDer(pem);
      return DerUtils.decodePrivateKey(der);
   }

   public static PrivateKey decodePrivateKey(InputStream is) throws Exception
   {
      String pem = pemFromStream(is);
      return decodePrivateKey(pem);
   }

   /**
    * Decode a PEM file to DER format
    *
    * @param is
    * @return
    * @throws IOException
    */
   public static byte[] pemToDer(InputStream is) throws IOException
   {
      String pem = pemFromStream(is);
      byte[] der = pemToDer(pem);
      return der;
   }

   /**
    * Decode a PEM string to DER format
    *
    * @param pem
    * @return
    * @throws java.io.IOException
    */
   public static byte[] pemToDer(String pem) throws java.io.IOException
   {
      pem = removeBeginEnd(pem);
      return Base64.decode(pem);
   }

   private static String removeBeginEnd(String pem)
   {
      pem = pem.replaceAll("-----BEGIN (.*)-----", "");
      pem = pem.replaceAll("-----END (.*)----", "");
      pem = pem.replaceAll("\r\n", "\n");
      return pem.trim();
   }


   public static String pemFromStream(InputStream is) throws IOException
   {
      DataInputStream dis = new DataInputStream(is);
      byte[] keyBytes = new byte[dis.available()];
      dis.readFully(keyBytes);
      dis.close();
      return new String(keyBytes, "utf-8");
   }
}