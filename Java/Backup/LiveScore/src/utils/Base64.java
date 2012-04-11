package neo.score.utils;


public class Base64
{

    public Base64()
    {
    }

    public static synchronized byte[] encode(byte abyte0[])
    {
        int i = 0;
        int j = 0;
        if(0 == abyte0.length)
            throw new IllegalArgumentException("cosaldap.Base64.encode(): zero-length encode argument!");
        int k = (abyte0.length / 3) * 4;
        if(0 != abyte0.length % 3)
            k = (k >> 2) + 1 << 2;
        byte abyte1[] = new byte[k];
        for(int l = abyte0.length; l > 0;)
        {
            byte byte6 = 0;
            abyte1[j + 3] = 61;
            abyte1[j + 2] = 61;
            switch(l)
            {
            default:
                byte byte3 = abyte0[i + 2];
                byte byte0 = (byte)(byte3 & 0x3f);
                abyte1[j + 3] = bin2ascii[byte0];
                byte6 = (byte)(byte3 >>> 6);
                byte6 &= 3;
                l--;
                // fall through

            case 2: // '\002'
                byte byte4 = abyte0[i + 1];
                byte byte1 = (byte)(byte4 << 2 | byte6);
                byte1 &= 0x3f;
                abyte1[j + 2] = bin2ascii[byte1];
                byte6 = (byte)(byte4 >>> 4);
                byte6 &= 0xf;
                l--;
                // fall through

            case 1: // '\001'
                byte byte5 = abyte0[i];
                byte byte2 = (byte)(byte5 << 4 | byte6);
                byte2 &= 0x3f;
                abyte1[j + 1] = bin2ascii[byte2];
                byte2 = (byte)(byte5 >>> 2 & 0x3f);
                abyte1[j] = bin2ascii[byte2];
                l--;
                j += 4;
                i += 3;
                break;
            }
        }

        return abyte1;
    }

    public static synchronized byte[] encode(String s)
    {
        return encode(s.getBytes());
    }

    public static synchronized byte[] decode(byte abyte0[])
    {
        int i = 0;
        int j = 0;
        boolean flag = false;
        int i1 = 0;
        byte abyte2[] = new byte[4];
        if(0 == abyte0.length)
            throw new IllegalArgumentException("cosaldap.Base64.decode(): Empty input");
        if(abyte0.length < 4)
        {
            byte abyte3[] = {
                61, 61, 61, 61
            };
            System.arraycopy(abyte0, 0, abyte3, 0, abyte0.length);
            abyte0 = abyte3;
        }
        int j1 = (abyte0.length / 4) * 3;
        byte abyte1[];
        for(abyte1 = new byte[j1]; j < abyte1.length;)
        {
            int k;
            for(k = 0; k < 4 && i < abyte0.length; k++)
            {
                abyte2[k] = 127;
                int l = abyte0[i++] - 43;
                if(l < 0 || l > 79)
                {
                    i1++;
                    k--;
                } else
                if(-1 != ascii2bin[l])
                {
                    abyte2[k] = ascii2bin[l];
                } else
                {
                    i1++;
                    k--;
                }
            }

            if(i == abyte0.length && 0 == j && 0 == k || i1 >= abyte0.length)
                throw new IllegalArgumentException("cosaldap.Base64.decoded(): Input not Base64 encoded");
            byte byte0 = abyte2[0];
            byte byte1 = abyte2[1];
            if(127 == byte1)
                break;
            abyte1[j++] = (byte)(byte0 << 2 | byte1 >> 4);
            byte byte2 = (byte)(byte1 << 4);
            byte1 = abyte2[2];
            if(127 == byte1)
                break;
            abyte1[j++] = (byte)(byte1 >>> 2 | byte2);
            byte2 = (byte)(byte1 << 6);
            byte1 = abyte2[3];
            if(127 == byte1)
                break;
            abyte1[j++] = (byte)(byte1 | byte2);
        }

        if(i1 > 0 || j < abyte1.length)
            if(j > 0)
            {
                byte abyte4[] = new byte[j];
                System.arraycopy(abyte1, 0, abyte4, 0, abyte4.length);
                abyte1 = abyte4;
            } else
            {
                abyte1 = new byte[0];
            }
        return abyte1;
    }

    public static synchronized byte[] decode(String s)
    {
        return decode(s.getBytes());
    }

    private static final String __me = "cosaldap.Base64";
    private static final byte PEM_PAD = 61;
    private static final byte PERR = -1;
    private static final byte PEOF = 127;
    private static final byte bin2ascii[] = {
        65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
        75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
        85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
        101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
        111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
        121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
        56, 57, 43, 47
    };
    private static final byte ascii2bin[] = {
        62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 
        57, 58, 59, 60, 61, -1, -1, -1, 127, -1, 
        -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 
        8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 
        18, 19, 20, 21, 22, 23, 24, 25, -1, -1, 
        -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 
        32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 
        42, 43, 44, 45, 46, 47, 48, 49, 50, 51
    };

}

