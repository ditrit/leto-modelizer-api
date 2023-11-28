import { isFieldAlreadyUsed } from 'src/cloud/utils';

describe('Test function: isFieldAlreadyUsed', () => {
  it('Should return false if the object is new and the field is not yet used', async () => {
    const request = {
      object: {
        existed: () => false,
      },
    };
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(0),
      }),
    };
    expect(isFieldAlreadyUsed(request, 'className', 'fieldName', 'fieldValue', Parse)).resolves.toEqual(false);
  });

  it('Should return false if this field is owned by the updated object', async () => {
    const request = {
      object: {
        existed: () => true,
      },
    };
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(1),
      }),
    };
    expect(isFieldAlreadyUsed(request, 'className', 'fieldName', 'fieldValue', Parse)).resolves.toEqual(false);
  });

  it('Should return true if the object is new and the field is already used by another object', async () => {
    const request = {
      object: {
        existed: () => false,
      },
    };
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(1),
      }),
    };
    expect(isFieldAlreadyUsed(request, 'className', 'fieldName', 'fieldValue', Parse)).resolves.toEqual(true);
  });

  it('Should return true if the field is already used by another object', async () => {
    const request = {
      object: {
        existed: () => true,
      },
    };
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(2),
      }),
    };
    expect(isFieldAlreadyUsed(request, 'className', 'fieldName', 'fieldValue', Parse)).resolves.toEqual(true);
  });
});
