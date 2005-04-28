#if !defined(eventInterfaceTypes_h)
#define eventInterfaceTypes_h

#include <linux/input.h>
#include <stdint.h>

#define getBit(bit, bitField) (bitField[bit/8] & (1 << (bit%8)))

struct input_devinfo {
        uint16_t bustype;
        uint16_t vendor;
        uint16_t product;
        uint16_t version;
};


/** removed for compatability with input.h --JPK
struct input_absinfo {
  int value;
  int minimum;
  int maximum;
  int fuzz;
  int flat;
};
*/

#endif //eventInterfaceTypes_h
