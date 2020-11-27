export class User {
  //might want to add tokenexpiration later
  constructor(
    public username: string,
    public id: string,
    private _token: string,
    public isAdmin: boolean,
    private expirationDate: Date
  ) {}

  get token() {
    if (!this.expirationDate || this.expirationDate < new Date()) {
      return null;
    }

    return this._token;
  }
}
