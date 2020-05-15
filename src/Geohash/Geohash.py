import string


class GeoHash:
    # initialize base32 characters for encoding
    def __init__(self):
        self.characters = {str(i): i for i in range(10)}
        exclude = {'a', 'l', 'o', 'i'}
        lower_cases = sorted(list(set(string.ascii_lowercase).difference(exclude)))
        self.characters.update({letter: i for i, letter in zip(range(10, 32), lower_cases)})
        self.letters = {idx: letter for letter, idx in self.characters.items()}

    # base32 geohash to binary string
    @staticmethod
    def hashcodeToBinary(characters: map, geohash: str) -> str:
        decoded = []
        for h in geohash:
            b = "{0:b}".format(characters[h])
            b = '0'*(5-len(b)) + b  # prefix with 0 if length < 5
            decoded.append(b)
        return "".join(decoded)

    def encode(self, latitude, longitude, precision) -> str:
        lng_start, lng_end = -180.0, 180.0
        lat_start, lat_end = -90.0, 90.0
        res = []
        even_bit = True
        bit_count = 0
        idx = 0
        while len(res) < precision:
            if even_bit:
                lng_mid = lng_start + (lng_end - lng_start) / 2
                if longitude > lng_mid:
                    idx = idx * 2 + 1
                    lng_start = lng_mid
                else:
                    idx *= 2
                    lng_end = lng_mid
            else:
                lat_mid = lat_start + (lat_end - lat_start) / 2
                if latitude > lat_mid:
                    idx = idx * 2 + 1
                    lat_start = lat_mid
                else:
                    idx *= 2
                    lat_end = lat_mid
            even_bit = not even_bit
            bit_count += 1
            if bit_count % 5 == 0:
                res.append(self.letters[idx])
                idx = 0
        return "".join(res)

    """
    @param: geohash: geohash a base32 string
    @return: latitude and longitude a location coordinate pair
    """
    def decode(self, geohash):
        decoded = GeoHash.hashcodeToBinary(self.characters, geohash)
        lng_start, lng_end = -180.0, 180.0
        lat_start, lat_end = -90.0, 90.0
        lng_ptr, lat_ptr = 0, 1
        while lng_ptr < len(decoded):
            mid = lng_start + (lng_end - lng_start) / 2
            if decoded[lng_ptr] == '1':
                lng_start = mid
            else:
                lng_end = mid
            lng_ptr += 2

        while lat_ptr < len(decoded):
            mid = lat_start + (lat_end - lat_start) / 2
            if decoded[lat_ptr] == '1':
                lat_start = mid
            else:
                lat_end = mid
            lat_ptr += 2

        return [(lat_start+lat_end) / 2, (lng_start+lng_end) / 2]


if __name__ == "__main__":
    geohasher = GeoHash()
    print(geohasher.encode(39.92816697, 116.38954991, 12))
