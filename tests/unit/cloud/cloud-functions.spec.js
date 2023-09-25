import { isGroupUnique } from 'src/cloud/cloud-functions.js';

describe('Test function: isGroupUnique', () => {
  it('should return a resolved promise when try to save a group that already exists', async () => {
    const Parse = {};
    await expect(isGroupUnique(true, 'myUniqueUnitTestGroupName', null, Parse))
      .resolves
      .toBeUndefined();
  });

  it('should throw an error when try to save a group with an unavailable name', async () => {
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(1),
      }),
    };

    await expect(isGroupUnique(false, 'myUnitTestGroupName', null, Parse))
      .rejects
      .toThrow('Group with this name already exists');
  });

  it('should throw an error when query.count is rejected', async () => {
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.reject(new Error('Count err')),
      }),
    };

    await expect(isGroupUnique(false, 'myUnitTestGroupName', null, Parse))
      .rejects
      .toThrow('Count err');
  });

  it('should return a resolved promise when try to save a group with an available name', async () => {
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(0),
      }),
    };

    await expect(isGroupUnique(false, 'myUniqueUnitTestGroupName', null, Parse))
      .resolves
      .toBeUndefined();
  });
});
