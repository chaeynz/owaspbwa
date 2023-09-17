"""File-system utils"""

import os


def create_file(path, content):
    """Create a file with the given content.

    @param path: The path to the file.
    @param content: The content to be written in the file.
    """
    fd = open(path, "w")
    fd.write(content)
    fd.close()


def read_file(path, limit=None):
    """Return the content of the given file.

    @param path: The path to the file.
    @param limit: An optional read limit. If positive, read up to that number
        of bytes from the beginning of the file. If negative, read up to that
        number of bytes from the end of the file.
    @return content: The content of the file, possibly trimmed to C{limit}.
    """
    fd = open(path, "r")
    if limit and os.path.getsize(path) > abs(limit):
        whence = 0
        if limit < 0:
            whence = 2
        fd.seek(limit, whence)
    content = fd.read()
    fd.close()
    return content


def touch_file(path):
    """Touch a file, creating it if it doesn't exist."""
    fd = open(path, "a")
    fd.close()
    os.utime(path, None)
