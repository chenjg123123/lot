import JSEncrypt from 'jsencrypt'

class RSAUtil {
  private static instance: RSAUtil
  private encrypt: JSEncrypt

  private constructor() {
    this.encrypt = new JSEncrypt()
    // 这里使用后端提供的公钥
    this.encrypt.setPublicKey(
      'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqhRMA9JwKm9G0Q/AuGDLVPjjADamOxcQsbBtoQzqK+CPMswG8CeChMJRh2xYYcpozat7PLXUoeQQJ3uaMi5Hi97VrobteC3ur6Wl4Z7xyY++p8sE0o9RvPttnaM+LsrsyCPDJyI4TpdmmnW+jbef5Opqa5d+XWrfeQZnPA/M88QIDAQAB',
    )
  }

  public static getInstance(): RSAUtil {
    if (!RSAUtil.instance) {
      RSAUtil.instance = new RSAUtil()
    }
    return RSAUtil.instance
  }

  // 加密数据
  public encryptData(data: string): string {
    return this.encrypt.encrypt(data) || ''
  }
}

export default RSAUtil.getInstance()
