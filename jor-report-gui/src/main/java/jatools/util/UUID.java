package jatools.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public final class UUID implements Serializable, Comparable {
    private static final long serialVersionUID = 0xbc9903f7986d852fL;
    private static volatile SecureRandom numberGenerator = null;
    private final long mostSigBits;
    private final long leastSigBits;
    private transient int version;
    private transient int variant;
    private volatile transient long timestamp;
    private transient int sequence;
    private transient long node;
    private transient int hashCode;

    private UUID(byte[] abyte0) {
        version = -1;
        variant = -1;
        timestamp = -1L;
        sequence = -1;
        node = -1L;
        hashCode = -1;

        long l = 0L;
        long l1 = 0L;

        for (int i = 0; i < 8; i++)
            l = (l << 8) | (long) (abyte0[i] & 0xff);

        for (int j = 8; j < 16; j++)
            l1 = (l1 << 8) | (long) (abyte0[j] & 0xff);

        mostSigBits = l;
        leastSigBits = l1;
    }

    /**
     * Creates a new UUID object.
     *
     * @param l DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     */
    public UUID(long l, long l1) {
        version = -1;
        variant = -1;
        timestamp = -1L;
        sequence = -1;
        node = -1L;
        hashCode = -1;
        mostSigBits = l;
        leastSigBits = l1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static UUID randomUUID() {
        SecureRandom securerandom = numberGenerator;

        if (securerandom == null) {
            numberGenerator = securerandom = new SecureRandom();
        }

        byte[] abyte0 = new byte[16];
        securerandom.nextBytes(abyte0);
        abyte0[6] &= 0xf;
        abyte0[6] |= 0x40;
        abyte0[8] &= 0x3f;
        abyte0[8] |= 0x80;

        UUID uuid = new UUID(abyte0);

        return new UUID(abyte0);
    }
    
    public static String newInstance()
    {
    	return randomUUID().toString() ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param abyte0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static UUID nameUUIDFromBytes(byte[] abyte0) {
        MessageDigest messagedigest;

        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new InternalError("MD5 not supported");
        }

        byte[] abyte1 = messagedigest.digest(abyte0);
        abyte1[6] &= 0xf;
        abyte1[6] |= 0x30;
        abyte1[8] &= 0x3f;
        abyte1[8] |= 0x80;

        return new UUID(abyte1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static UUID fromString(String s) {
        String[] as = s.split("-");

        if (as.length != 5) {
            throw new IllegalArgumentException((new StringBuffer()).append("Invalid UUID string: ")
                                                .append(s).toString());
        }

        for (int i = 0; i < 5; i++)
            as[i] = (new StringBuffer()).append("0x").append(as[i]).toString();

        long l = Long.decode(as[0]).longValue();
        l <<= 16;
        l |= Long.decode(as[1]).longValue();
        l <<= 16;
        l |= Long.decode(as[2]).longValue();

        long l1 = Long.decode(as[3]).longValue();
        l1 <<= 48;
        l1 |= Long.decode(as[4]).longValue();

        return new UUID(l, l1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getLeastSignificantBits() {
        return leastSigBits;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getMostSignificantBits() {
        return mostSigBits;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int version() {
        if (version < 0) {
            version = (int) ((mostSigBits >> 12) & 15L);
        }

        return version;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int variant() {
        if (variant < 0) {
            if (leastSigBits >>> 63 == 0L) {
                variant = 0;
            } else if (leastSigBits >>> 62 == 2L) {
                variant = 2;
            } else {
                variant = (int) (leastSigBits >>> 61);
            }
        }

        return variant;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long timestamp() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }

        long l = timestamp;

        if (l < 0L) {
            l = (mostSigBits & 4095L) << 48;
            l |= (((mostSigBits >> 16) & 65535L) << 32);
            l |= mostSigBits >>> 32;
            timestamp = l;
        }

        return l;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int clockSequence() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }

        if (sequence < 0) {
            sequence = (int) ((leastSigBits & 0x3fff000000000000L) >>> 48);
        }

        return sequence;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long node() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }

        if (node < 0L) {
            node = leastSigBits & 0xffffffffffffL;
        }

        return node;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (new StringBuffer()).append(digits(mostSigBits >> 32, 8)).append("-")
                .append(digits(mostSigBits >> 16, 4)).append("-").append(digits(mostSigBits, 4))
                .append("-").append(digits(leastSigBits >> 48, 4)).append("-")
                .append(digits(leastSigBits, 12)).toString();
    }

    private static String digits(long l, int i) {
        long l1 = 1L << (i * 4);

        return Long.toHexString(l1 | (l & (l1 - 1L))).substring(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        if (hashCode == -1) {
            hashCode = (int) ((mostSigBits >> 32) ^ mostSigBits ^ (leastSigBits >> 32) ^
                leastSigBits);
        }

        return hashCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof UUID)) {
            return false;
        }

        if (((UUID) obj).variant() != variant()) {
            return false;
        } else {
            UUID uuid = (UUID) obj;

            return (mostSigBits == uuid.mostSigBits) && (leastSigBits == uuid.leastSigBits);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param uuid DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(UUID uuid) {
        return (mostSigBits >= uuid.mostSigBits)
        ? ((mostSigBits <= uuid.mostSigBits)
        ? ((leastSigBits >= uuid.leastSigBits)
        ? ((byte) (((byte) ((leastSigBits <= uuid.leastSigBits) ? 0 : 1)))) : (-1)) : 1) : (-1);
    }

    private void readObject(ObjectInputStream objectinputstream)
        throws IOException, ClassNotFoundException {
        objectinputstream.defaultReadObject();
        version = -1;
        variant = -1;
        timestamp = -1L;
        sequence = -1;
        node = -1L;
        hashCode = -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Object obj) {
        return compareTo((UUID) obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println(randomUUID());
    }
}
