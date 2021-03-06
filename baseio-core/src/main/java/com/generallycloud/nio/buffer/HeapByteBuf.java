package com.generallycloud.nio.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.generallycloud.nio.common.MathUtil;
import com.generallycloud.nio.component.SocketChannel;

public class HeapByteBuf extends AbstractByteBuf {

	protected byte[]	memory;

	protected HeapByteBuf(byte[] memory) {
		super(memory.length);
		this.memory = memory;
	}

	public HeapByteBuf(ByteBufAllocator allocator, byte[] memory) {
		this(allocator, memory, new ReferenceCount());
	}

	public HeapByteBuf(ByteBufAllocator allocator, byte[] memory, ReferenceCount referenceCount) {
		super(allocator, referenceCount);
		this.memory = memory;
	}

	public byte[] array() {
		return memory;
	}

	protected AbstractByteBuf newByteBuf() {
		return new HeapByteBuf(allocator, memory, referenceCount);
	}

	public byte getByte(int index) {
		return memory[ix(index)];
	}

	public void get(byte[] dst, int offset, int length) {
		System.arraycopy(memory, ix(position), dst, offset, length);
		this.position += length;
	}

	public int getInt() {
		int v = MathUtil.byte2Int(memory, ix(position));
		this.position += 4;
		return v;
	}

	public int getInt(int index) {
		return MathUtil.byte2Int(memory, ix(index));
	}

	public long getLong() {
		long v = MathUtil.byte2Long(memory, ix(position));
		this.position += 8;
		return v;
	}

	public long getLong(int index) {
		return MathUtil.byte2Long(memory, ix(index));
	}

	public boolean hasArray() {
		return true;
	}

	public void put(byte[] src, int offset, int length) {
		System.arraycopy(src, offset, memory, ix(position), length);
		;
		this.position += length;
	}

	public int read(ByteBuffer buffer) throws IOException {

		int srcRemaining = buffer.remaining();

		if (srcRemaining == 0) {
			return 0;
		}

		int remaining = this.remaining();

		if (remaining <= srcRemaining) {

			buffer.get(memory, ix(position), remaining);

			this.position(this.limit);

			return remaining;
		} else {

			buffer.get(memory, ix(position), srcRemaining);

			this.position(this.position + srcRemaining);

			return srcRemaining;
		}
	}

	public int write(SocketChannel channel) throws IOException {

		int length = channel.write(nioBuffer);

		if (length > 0) {

			position += length;

			channel.upNetworkState();

		} else {

			channel.downNetworkState();
		}

		return length;
	}

	public byte getByte() {
		return memory[ix(position++)];
	}

	public int forEachByte(int index, int length, ByteProcessor processor) {

		byte[] array = memory;

		int start = ix(index);

		int end = start + length;

		try {

			for (int i = start; i < end; i++) {

				if (!processor.process(array[i])) {

					return i - start;
				}

			}

		} catch (Exception e) {
		}

		return -1;
	}

	public int forEachByteDesc(int index, int length, ByteProcessor processor) {

		byte[] array = memory;

		int start = ix(index);

		int end = start + length;

		try {

			for (int i = end; i >= start; i--) {

				if (!processor.process(array[i])) {

					return i - start;
				}

			}

		} catch (Exception e) {
		}

		return -1;
	}

	public void putByte(byte b) {
		memory[ix(position++)] = b;
	}

	public int read(ByteBuf buf) throws IOException {

		int srcRemaining = buf.remaining();

		if (srcRemaining == 0) {
			return 0;
		}

		int remaining = this.remaining();

		if (remaining <= srcRemaining) {

			buf.get(memory, ix(position), remaining);

			position(limit);

			return remaining;
		} else {

			buf.get(memory, ix(position), srcRemaining);

			position(position + srcRemaining);

			return srcRemaining;
		}
	}

	public int getIntLE() {
		int v = MathUtil.byte2IntLE(memory, ix(position));
		this.position += 4;
		return v;
	}

	public int getIntLE(int offset) {
		return MathUtil.byte2IntLE(memory, ix(offset));
	}

	public long getLongLE() {
		long v = MathUtil.byte2LongLE(memory, ix(position));
		this.position += 8;
		return v;
	}

	public long getLongLE(int index) {
		return MathUtil.byte2LongLE(memory, ix(index));
	}

	public int getShort() {
		int v = MathUtil.byte2Short(memory, ix(position));
		this.position += 2;
		return v;
	}

	public int getShort(int index) {
		return MathUtil.byte2Short(memory, ix(index));
	}

	public int getShortLE() {
		int v = MathUtil.byte2ShortLE(memory, ix(position));
		this.position += 2;
		return v;
	}

	public int getShortLE(int index) {
		return MathUtil.byte2ShortLE(memory, ix(index));
	}

	public int getUnsignedByte() {
		return getByte() & 0xff;
	}

	public int getUnsignedByte(int index) {
		return getByte(index) & 0xff;
	}

	public long getUnsignedInt() {
		long v = MathUtil.byte2UnsignedInt(memory, ix(position));
		this.position += 4;
		return v;
	}

	public long getUnsignedInt(int index) {
		return MathUtil.byte2UnsignedInt(memory, ix(index));
	}

	public long getUnsignedIntLE() {
		long v = MathUtil.byte2UnsignedIntLE(memory, ix(position));
		this.position += 4;
		return v;
	}

	public long getUnsignedIntLE(int index) {
		return MathUtil.byte2UnsignedIntLE(memory, ix(index));
	}

	public int getUnsignedShort() {
		int v = MathUtil.byte2UnsignedShort(memory, ix(position));
		this.position += 2;
		return v;
	}

	public int getUnsignedShort(int index) {
		return MathUtil.byte2UnsignedShort(memory, ix(index));
	}

	public int getUnsignedShortLE() {
		int v = MathUtil.byte2UnsignedShortLE(memory, ix(position));
		this.position += 2;
		return v;
	}

	public int getUnsignedShortLE(int index) {
		return MathUtil.byte2UnsignedShortLE(memory, ix(index));
	}

	protected ByteBuffer getNioBuffer() {
		if (nioBuffer == null) {
			nioBuffer = ByteBuffer.wrap(memory, offset, capacity);
		}
		return nioBuffer;
	}

	public void putShort(short value) {
		MathUtil.short2Byte(memory, value, ix(position));
		position +=2;
	}

	public void putShortLE(short value) {
		MathUtil.short2ByteLE(memory, value, ix(position));
		position +=2;
	}

	public void putUnsignedShort(int value) {
		MathUtil.unsignedShort2Byte(memory, value, ix(position));
		position +=2;
	}

	public void putUnsignedShortLE(int value) {
		MathUtil.unsignedShort2ByteLE(memory, value, ix(position));
		position +=2;
	}

	public void putInt(int value) {
		MathUtil.int2Byte(memory, value, ix(position));
		position +=4;
	}

	public void putIntLE(int value) {
		MathUtil.int2ByteLE(memory, value, ix(position));
		position +=4;
	}

	public void putUnsignedInt(long value) {
		MathUtil.unsignedInt2Byte(memory, value, ix(position));
		position +=4;
	}

	public void putUnsignedIntLE(long value) {
		MathUtil.unsignedInt2ByteLE(memory, value, ix(position));
		position +=4;
	}

	public void putLong(long value) {
		MathUtil.long2Byte(memory, value, ix(position));
		position +=8;
	}

	public void putLongLE(long value) {
		MathUtil.long2ByteLE(memory, value, ix(position));
		position +=8;
	}
	
}
