package jatools.imageio;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.OutputStream;


public class GifEncoder {
   
   short imageWidth;
   short imageHeight;
   
   int numberOfColors;
   
   byte[] allPixels = null;
  
   byte[] allColors = null;

   
   public GifEncoder( Image image )
   throws AWTException
   {
      this.imageWidth = (short)image.getWidth( null );
      this.imageHeight = (short)image.getHeight( null );

      int values[] = new int[this.imageWidth * this.imageHeight];
      PixelGrabber grabber = new PixelGrabber( image,
                                               0, 0,
                                               this.imageWidth,
                                               this.imageHeight,
                                               values, 0, this.imageWidth );

      try
         {
         if ( grabber.grabPixels() != true )
            throw new AWTException( "Grabber returned false: " +
                                    grabber.status() );
         } 

      catch ( InterruptedException ie )
         {
         }

      byte[][] r = new byte[ this.imageWidth ][ this.imageHeight ];
      byte[][] g = new byte[ this.imageWidth ][ this.imageHeight ];
      byte[][] b = new byte[ this.imageWidth ][ this.imageHeight ];
      int index = 0;

      for ( int y=0; y<this.imageHeight; y++ )
         {
         for ( int x=0; x<this.imageWidth; x++, index++ )
            {
            r[ x ][ y ] = (byte)( ( values[ index ] >> 16 ) & 0xFF );
            g[ x ][ y ] = (byte)( ( values[ index ] >> 8 ) & 0xFF );
            b[ x ][ y ] = (byte)( ( values[ index ] ) & 0xFF );
            } // ends for

         } 

      this.toIndexColor( r, g, b );
   } 
   public GifEncoder( byte[][] r, byte[][] g, byte[][] b )
   throws AWTException
   {
      this.imageWidth = (short)( r.length );
      this.imageHeight = (short)( r[0].length );

      this.toIndexColor( r, g, b );
   } 
   public void write( OutputStream output )
   throws IOException
   {
      BitUtils.writeString( output, "GIF87a" );
      ScreenDescriptor sd = new ScreenDescriptor( this.imageWidth,
                                                  this.imageHeight, this.numberOfColors );
      sd.write( output );

      output.write( this.allColors, 0, this.allColors.length) ;

      ImageDescriptor id = new ImageDescriptor( this.imageWidth, this.imageHeight, ',' );
      id.write( output );

      byte codesize = BitUtils.BitsNeeded(this.numberOfColors);
      if ( codesize == 1 )
         {
         codesize ++;
         }
      output.write( codesize );

      LZWCompressor.LZWCompress( output, codesize, this.allPixels );
      output.write( 0 );

      id = new ImageDescriptor( (byte)0, (byte)0, ';' );
      id.write( output );
      output.flush();
   } 

   /**
	 * Converts rgb desrcription of image to colour number description used by
	 * GIF.
	 * 
	 * @param r
	 *            red array of pixels
	 * @param g
	 *            green array of pixels
	 * @param b
	 *            blue array of pixels
	 * @exception AWTException
	 *                Thrown if too many different colours in image.
	 */
   void toIndexColor( byte[][] r, byte[][] g, byte[][] b )
   throws AWTException
   {
      this.allPixels = new byte[this.imageWidth * this.imageHeight];
      this.allColors = new byte[256 * 3];
      int colornum = 0;
      for ( int x=0; x<this.imageWidth; x++ )
         {
         for ( int y=0; y<this.imageHeight; y++ )
            {
            int search;
            for ( search=0; search < colornum; search++ )
               {
               if ( this.allColors[search * 3] == r[x][y] &&
                    this.allColors[search * 3 + 1] == g[x][y] &&
                    this.allColors[search * 3 + 2] == b[x][y] )
                  {
                  break;
                  } 

               }

            if ( search > 255 )
               throw new AWTException( "Too many colors." );
            
            this.allPixels[ y * this.imageWidth + x ] = (byte)search;

            if ( search == colornum )
               {
               this.allColors[ search * 3 ] = r[x][y]; // [col][row]
               this.allColors[ search * 3 + 1 ] = g[x][y];
               this.allColors[ search * 3 + 2 ] = b[x][y];
               colornum++;
               } 

            } 

         } 

      this.numberOfColors = 1 << BitUtils.BitsNeeded( colornum );
      byte copy[] = new byte[this.numberOfColors * 3];
      System.arraycopy( this.allColors, 0, copy, 0, this.numberOfColors * 3 );
      this.allColors = copy;
   } 

   }

/**
 * Used to write the bits composing the GIF image.
 */
class BitFile extends Object
   {
   /**
	 * The outputstream where the data is written.
	 */
   OutputStream output = null;
   /**
	 * buffer for bits to write.
	 */
   byte[] buffer;
   /**
    */
   int indexIntoOutputStream;
   /**
    */
   int bitsLeft;

   /**
	 * constructor
	 * 
	 * @param output
	 *            Where image will be written
	 */
   public BitFile( OutputStream output )
      {
      this.output = output;
      this.buffer = new byte[ 256 ];
      this.indexIntoOutputStream = 0;
      this.bitsLeft = 8;
      } // ends constructor BitFile(OutputStream )

   /**
	 * Ensures image in ram gets to disk.
	 * 
	 * @exception IOException
	 */
   public void flush( )
   throws IOException
   {
      int numBytes = this.indexIntoOutputStream + ( (this.bitsLeft == 8 ) ? 0 : 1 );
      if ( numBytes > 0 )
         {
         this.output.write( numBytes );
         this.output.write( this.buffer, 0, numBytes );
         this.buffer[0] = 0;
         this.indexIntoOutputStream = 0;
         this.bitsLeft = 8;
         } // ends if

   } // ends flush( void )

   /**
	 * Write bits to stream.
	 * 
	 * @param bits
	 *            source of bits, low/high order?
	 * @param numbits
	 *            how many bits to write.
	 * 
	 * @exception IOException
	 */
   public void writeBits( int bits, int numbits )
   throws IOException
   {
      int bitsWritten = 0;
      int numBytes = 255;
      do
         {
         if ( (this.indexIntoOutputStream == 254 && this.bitsLeft == 0 ) ||
              this.indexIntoOutputStream > 254 )
            {
            this.output.write( numBytes );
            this.output.write( this.buffer, 0, numBytes );

            this.buffer[0] = 0;
            this.indexIntoOutputStream = 0;
            this.bitsLeft = 8;
            } // ends if

         if ( numbits <= this.bitsLeft )
            {
            this.buffer[this.indexIntoOutputStream] |= ( bits & ( ( 1 << numbits ) - 1 ) ) << ( 8 - this.bitsLeft );
            bitsWritten += numbits;
            this.bitsLeft -= numbits;
            numbits = 0;
            } // ends if

         else
            {
            this.buffer[ this.indexIntoOutputStream ] |= ( bits & ( ( 1 << this.bitsLeft ) - 1 ) ) << ( 8 - this.bitsLeft );
            bitsWritten += this.bitsLeft;
            bits >>= this.bitsLeft;
            numbits -= this.bitsLeft;
            this.buffer[ ++this.indexIntoOutputStream ] = 0;
            this.bitsLeft = 8;
            } // ends else

         } while ( numbits != 0 );

   } // ends writeBits( int, int )

   } // ends class BitFile

/**
 * Used to compress the image by looking for repeating elements.
 */
class LZWStringTable extends Object
   {
   private final static int RES_CODES = 2;
   private final static short HASH_FREE = (short ) 0xFFFF;
   private final static short NEXT_FIRST = (short ) 0xFFFF;
   private final static int MAXBITS = 12;
   private final static int MAXSTR = ( 1 << MAXBITS );
   private final static short HASHSIZE = 9973;
   private final static short HASHSTEP = 2039;

   byte strChr_[];
   short strNxt_[];
   short strHsh_[];
   short numStrings_;

   public LZWStringTable( )
      {
      strChr_ = new byte[MAXSTR];
      strNxt_ = new short[MAXSTR];
      strHsh_ = new short[HASHSIZE];
      } // ends constructor LZWStringTable( void )

   public int addCharString( short index, byte b )
      {
      int hshidx;
      if ( numStrings_ >= MAXSTR )
         return 0xFFFF;

      hshidx = Hash( index, b );
      while ( strHsh_[hshidx] != HASH_FREE )
         hshidx = ( hshidx + HASHSTEP ) % HASHSIZE;

      strHsh_[hshidx] = numStrings_;
      strChr_[numStrings_] = b;
      strNxt_[numStrings_] = ( index != HASH_FREE ) ? index : NEXT_FIRST;

      return numStrings_++;
      } // ends addCharString( short, byte )

   public short findCharString( short index, byte b )
      {
      int hshidx, nxtidx;

      if ( index == HASH_FREE )
         return b;

      hshidx = Hash( index, b );
      while ( ( nxtidx = strHsh_[hshidx] ) != HASH_FREE )
         {
         if ( strNxt_[nxtidx] == index && strChr_[nxtidx] == b )
            return(short ) nxtidx;
         hshidx = ( hshidx + HASHSTEP ) % HASHSIZE;
         } // ends while

      return(short ) 0xFFFF;
      } // ends findCharString( short, byte )

   public void ClearTable( int codesize )
      {
      numStrings_ = 0;

      for ( int q = 0; q < HASHSIZE; q++ )
         strHsh_[q] = HASH_FREE;

      int w = ( 1 << codesize ) + RES_CODES;
      for ( int q = 0; q < w; q++ )
         this.addCharString( (short ) 0xFFFF, (byte)q );
      } // ends ClearTable(int)

   public static int Hash( short index, byte lastbyte )
      {
      return( (int)( (short ) ( lastbyte << 8 ) ^ index ) & 0xFFFF ) %
      HASHSIZE;
      }

   } // ends class LZWStringTable

/**
 * Used to compress the image by looking for repeated elements.
 */
class LZWCompressor extends Object
   {
   public static void LZWCompress( OutputStream output, int codesize, byte
                                   toCompress[] )
   throws IOException
   {
      byte c;
      short index;
      int clearcode, endofinfo, numbits, limit, errcode;
      short prefix = (short ) 0xFFFF;

      BitFile bitFile = new BitFile( output );
      LZWStringTable strings = new LZWStringTable( );

      clearcode = 1 << codesize;
      endofinfo = clearcode + 1;

      numbits = codesize + 1;
      limit = ( 1 << numbits ) - 1;

      strings.ClearTable( codesize );
      bitFile.writeBits( clearcode, numbits );

      for ( int loop = 0; loop < toCompress.length; loop++ )
         {
         c = toCompress[loop];
         if ( ( index = strings.findCharString( prefix, c ) ) != -1 )
            prefix = index;
         else
            {
            bitFile.writeBits( prefix, numbits );
            if ( strings.addCharString( prefix, c ) > limit )
               {
               if ( ++numbits > 12 )
                  {
                  bitFile.writeBits( clearcode, numbits - 1 );
                  strings.ClearTable( codesize );
                  numbits = codesize + 1;
                  } // ends if

               limit = ( 1 << numbits ) - 1;
               } // ends if

            prefix = (short ) ( (short ) c & 0xFF );
            } // ends else

         } // ends for

      if ( prefix != -1 )
         bitFile.writeBits( prefix, numbits );

      bitFile.writeBits( endofinfo, numbits );
      bitFile.flush( );
   } // ends LZWCompress( OutputStream, int, byte[] )

   } // ends class LWZCompressor

/**
 */
class ScreenDescriptor extends Object
   {
   public short localScreenWidth;
   public short localScreenHeight;
   private byte currentByte;
   public byte backgroundColorIndex;
   public byte pixelAspectRatio;

   /**
	 * tool for dumping current screen image??
	 * 
	 * @param width
	 * @param height
	 * @param numColors
	 */
   public ScreenDescriptor( short width, short height, int numColors )
      {
      this.localScreenWidth = width;
      this.localScreenHeight = height;
      SetGlobalColorTableSize( (byte)( BitUtils.BitsNeeded( numColors ) -
                                       1 ) );
      SetGlobalColorTableFlag( (byte)1 );
      SetSortFlag( (byte)0 );
      SetColorResolution( (byte)7 );
      this.backgroundColorIndex = 0;
      this.pixelAspectRatio = 0;
      } // ends constructor ScreenDescriptor( short, short, int )

   public void write(OutputStream output )
   throws IOException
   {
      BitUtils.writeWord( output, this.localScreenWidth );
      BitUtils.writeWord( output, this.localScreenHeight );
      output.write( this.currentByte );
      output.write( this.backgroundColorIndex );
      output.write( this.pixelAspectRatio );
   } // ends write( OutputStream )

   public void SetGlobalColorTableSize( byte num )
      {
      this.currentByte |= ( num & 7 );
      }

   public void SetSortFlag( byte num )
      {
      this.currentByte |= ( num & 1 ) << 3;
      }

   public void SetColorResolution( byte num )
      {
      this.currentByte |= ( num & 7 ) << 4;
      }

   public void SetGlobalColorTableFlag( byte num )
      {
      this.currentByte |= ( num & 1 ) << 7;
      }

   } // ends class ScreenDescriptor

/**
 */
class ImageDescriptor extends Object
   {
   public byte separator_;
   public short leftPosition;
   public short topPosition;
   public short imageWidth;
   public short imageHeight;
   private byte currentByte;

   /**
	 * ???
	 * 
	 * @param width
	 * @param height
	 * @param separator
	 */
   public ImageDescriptor( short width, short height, char separator )
      {
      separator_ = (byte)separator;
      this.leftPosition = 0;
      this.topPosition = 0;
      this.imageWidth = width;
      this.imageHeight = height;
      SetLocalColorTableSize( (byte)0 );
      SetReserved( (byte)0 );
      SetSortFlag( (byte)0 );
      SetInterlaceFlag( (byte)0 );
      SetLocalColorTableFlag( (byte)0 );
      } // ends constructor ImageDescriptor( short, short, char )

   public void write( OutputStream output )
   throws IOException
   {
      output.write( separator_ );
      BitUtils.writeWord( output, this.leftPosition );
      BitUtils.writeWord( output, this.topPosition );
      BitUtils.writeWord( output, this.imageWidth );
      BitUtils.writeWord( output, this.imageHeight );
      output.write( this.currentByte );
   } // ends write( OutputStream )

   public void SetLocalColorTableSize( byte num )
      {
      this.currentByte |= ( num & 7 );
      }

   public void SetReserved( byte num )
      {
      this.currentByte |= ( num & 3 ) << 3;
      }

   public void SetSortFlag( byte num )
      {
      this.currentByte |= ( num & 1 ) << 5;
      }

   public void SetInterlaceFlag( byte num )
      {
      this.currentByte |= ( num & 1 ) << 6;
      }

   public void SetLocalColorTableFlag( byte num )
      {
      this.currentByte |= ( num & 1 ) << 7;
      }

   } // ends class ImageDescriptor

class BitUtils extends Object
   {
   /**
	 * Bits needed no represent the number n???
	 */
   public static byte BitsNeeded( int n )
      {
      byte ret = 1;

      if ( n-- == 0 )
         return 0;

      while ( ( n >>= 1 ) != 0 )
         ret++;

      return ret;
      } // ends BitsNeeded(int)

   public static void writeWord( OutputStream output, short w ) throws IOException
   {
      output.write( w & 0xFF );
      output.write( ( w >> 8 ) & 0xFF );
   } // ends writeWord( OutputStream, short )

   static void writeString( OutputStream output, String string ) throws IOException
   {
      for ( int loop = 0; loop < string.length(); loop++ )
         {
         output.write( (byte)( string.charAt( loop ) ) );
         }
   } // ends writeString( OutputStream, String )

   } // ends class BitUtils
